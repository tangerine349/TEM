package club.thom.tem.hypixel.request;

import club.thom.tem.TEM;
import club.thom.tem.models.inventory.PlayerData;
import club.thom.tem.storage.TEMConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkyblockPlayerRequest extends Request {
    private static final Logger logger = LogManager.getLogger(SkyblockPlayerRequest.class);
    final CompletableFuture<PlayerData> future = new CompletableFuture<>();
    final String uuid;
    TEMConfig config;

    public SkyblockPlayerRequest(TEM tem, String uuid, Map<String, String> headers, String requestScheme) {
        super(tem, "skyblock/profiles", headers, requestScheme);
        this.uuid = uuid;
        this.config = tem.getConfig();
    }

    @Override
    public HashMap<String, String> generateParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("uuid", uuid);
        parameters.put("key", config.getHypixelKey());
        return parameters;
    }

    @Override
    public void makeRequest() {
        RequestData data = requestToReturnedData();
        if (data == null) {
            return;
        }
        logger.debug("Creating player data.");
        PlayerData playerData = new PlayerData(getTem(), data.getJsonAsObject(), uuid);
        logger.debug("Completing future...");
        future.complete(playerData);
        if (data.getStatus() != 200) {
            logger.error("Code: {} while making player request...", data.getStatus());
        }
    }

    @Override
    public CompletableFuture<PlayerData> getFuture() {
        return future;
    }
}

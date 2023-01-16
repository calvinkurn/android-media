package com.tokopedia.contactus.createticket.utilities;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;

public class LoggingOnNewRelic {
    private static final String NEW_RELIC_LOG_TAG = "CONTACT_US_FAQ_PAGE";
    private static final String NEW_RELIC_CREATE_LOG_TAG = "CONTACT_US_CREATE_TICKET";
    public static final String ACTION_SEND_CREATED_TICKET = "send_created_ticket";
    public static final String ACTION_CREATE_TICKET = "create_ticket";

    public void sendToNewRelicLog(String url) {
        HashMap<String, String> map = new HashMap<>();
        map.put("urlPath", url);
        ServerLogger.log(Priority.P2, NEW_RELIC_LOG_TAG, map);
    }

    public void sendToNewRelicLog(String url, String action, String solutionId, String tags) {
        HashMap<String, String> map = new HashMap<>();
        map.put("urlPath", url);
        map.put("action", action);
        map.put("solutionId", solutionId);
        map.put("tags", tags);
        ServerLogger.log(Priority.P2, NEW_RELIC_LOG_TAG, map);
    }

    public void sendToNewRelicLog(String url, String chatRoute) {
        HashMap<String, String> map = new HashMap<>();
        map.put("urlPath", url);
        map.put("action", "to_chat_bot");
        map.put("addressChat", chatRoute);
        ServerLogger.log(Priority.P2, NEW_RELIC_LOG_TAG, map);
    }

    public void sendToNewRelicLog(String action, String solutionId, String tags) {
        HashMap<String, String> map = new HashMap<>();
        map.put("action", action);
        map.put("solutionId", solutionId);
        map.put("tags", tags);
        ServerLogger.log(Priority.P2, NEW_RELIC_CREATE_LOG_TAG, map);
    }
}

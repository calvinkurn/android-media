package com.tokopedia.groupchat.chatroom.websocket;

import com.google.gson.JsonObject;

/**
 * @author : Steven 08/10/18
 */
public class GroupChatWebSocketParam {

     static final String TYPE = "type";
     static final String DATA = "data";
     static final String SEND = "SEND_MESG";
     static final String ERROR = "ERROR";
     static final String CHANNEL_ID = "channel_id";
     static final String MESSAGE = "message";


     public static String getParamSend(String channelId, String message) {
        JsonObject json = new JsonObject();
        json.addProperty(TYPE, SEND);
        json.add(DATA, getParamData(channelId, message));
        return json.toString();
    }

    private static JsonObject getParamData(String channelId, String message) {
        JsonObject data = new JsonObject();
        data.addProperty(CHANNEL_ID, Integer.valueOf(channelId));
        data.addProperty(MESSAGE, message);
        return data;
    }
}

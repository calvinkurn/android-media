package com.tokopedia.websocket

import com.google.gson.JsonObject

/**
 * @author : Steven 08/10/18
 */
object GroupChatWebSocketParam {

    internal val TYPE = "type"
    internal val DATA = "data"
    internal val SEND = "SEND_MESG"
    internal val ERROR = "ERROR"
    internal val CHANNEL_ID = "channel_id"
    internal val MESSAGE = "message"


    fun getParamSend(channelId: String, message: String): String {
        val json = JsonObject()
        json.addProperty(TYPE, SEND)
        json.add(DATA, getParamData(channelId, message))
        return json.toString()
    }

    private fun getParamData(channelId: String, message: String): JsonObject {
        val data = JsonObject()
        data.addProperty(CHANNEL_ID, Integer.valueOf(channelId))
        data.addProperty(MESSAGE, message)
        return data
    }
}

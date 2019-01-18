package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.topchat.common.InboxChatConstant.UPLOADING

/**
 * @author : Steven 01/01/19
 */

object TopChatWebSocketParam {

    fun generateParamSendMessage(thisMessageId: String, messageText: String, startTime: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(thisMessageId))
        data.addProperty("message", messageText)
        data.addProperty("start_time", startTime)
        json.add("data", data)
        return json.toString()
    }


    fun generateParamSendImage(messageId: String, path: String, startTime: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", UPLOADING)
        data.addProperty("start_time", startTime)
        data.addProperty("file_path", path)
        data.addProperty("attachment_type", Integer.parseInt(TYPE_IMAGE_UPLOAD))
        json.add("data", data)
        return json.toString()
    }

    fun generateParamStartTyping(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(thisMessageId))
        json.add("data", data)
        return json.toString()
    }

    fun generateParamStopTyping(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(thisMessageId))
        json.add("data", data)
        return json.toString()
    }

    fun generateParamRead(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(thisMessageId))
        json.add("data", data)
        return json.toString()
    }

}
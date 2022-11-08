package com.tokopedia.chatbot.domain

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_REPLY_BUBBLE
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_SECURE_IMAGE_UPLOAD
import com.tokopedia.chatbot.util.convertMessageIdToLong
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object ChatbotSendWebsocketParam {

    fun generateParamSendMessage(
        messageId: String, sendMessage: String, startTime: String, toUid
        : String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("message", sendMessage)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)
        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
        json.add("data", data)
        return json
    }

    fun generateParamSendImage(
        messageId: String,
        path: String,
        imageObj: String,
        startTime: String,
        toUid: String
    ): JsonObject {

        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("message", "Uploaded Image")
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty("file_path", path)
        data.addProperty("image_obj", imageObj)
        data.addProperty("attachment_type", TYPE_IMAGE_UPLOAD.toIntOrZero())
        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
        json.add("data", data)
        return json
    }

    fun generateParamUploadSecureSendImage(
            messageId: String,
            path: String,
            startTime: String,
            toUid: String,
            name: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id",messageId.convertMessageIdToLong())
        data.addProperty("from", name)
        data.addProperty("from_user_name", name)
        data.addProperty("message", "Uploaded Image")
        data.addProperty("start_time", startTime)
        data.addProperty("file_path", path)
        data.addProperty("attachment_type", TYPE_SECURE_IMAGE_UPLOAD.toIntOrZero())
        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
        json.add("data", data)
        return json
    }

    fun generateParamSendMessageWithReplyBubble(
        messageId: String,
        message: String,
        startTime: String,
        referredMsg: ParentReply? = null
    ): JsonObject {
        val referredMsgObj = generateReferredMsg(referredMsg)
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message", message)
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("attachment_type", TYPE_REPLY_BUBBLE.toIntOrZero())
        data.addProperty("start_time", startTime)
        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
        if (referredMsgObj != null)
            data.add("parent_reply", referredMsgObj)
        json.add("data", data)
        return json
    }

    private fun generateReferredMsg(referredMsg: ParentReply?): JsonElement? {
        if (referredMsg == null)
            return null

        val request = JsonObject()
        request.addProperty("sender_id",referredMsg.senderId.toLongOrZero())
        request.addProperty("reply_time",referredMsg.replyTime.toLongOrZero())
        request.addProperty("main_text",referredMsg.mainText)
        request.addProperty("name", referredMsg.name)
        return request
    }

}

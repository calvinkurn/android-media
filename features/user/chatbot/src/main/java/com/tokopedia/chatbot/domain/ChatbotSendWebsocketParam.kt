package com.tokopedia.chatbot.domain

import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE

object ChatbotSendWebsocketParam {

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
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", "Uploaded Image")
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty("file_path", path)
        data.addProperty("image_obj", imageObj)
        data.addProperty("attachment_type", Integer.parseInt(TYPE_IMAGE_UPLOAD))
        json.add("data", data)
        return json
    }

}

package com.tokopedia.chatbot.domain

import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_SECURE_IMAGE_UPLOAD
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
    fun generateParamUploadSecureSendImage(
        messageId: String,
        path: String,
        startTime: String,
        toUid: String,
        name: String
    ): JsonObject {
//
//        "code":103,
//        "data":{
//            "attachment_type":26,
//            "file_path":"https://chat-staging.tokopedia.com/tc/v1/download_secure/4021658/2021-09-07/7a4d2456-0fe9-11ec-ac91-00163e041655",
//            "from":"bcdua",
//            "from_user_name":"bcdua",
//            "message":"Uploaded Image",
//            "message_id":4013580,
//            "start_time":"2021-09-07T12:30:17.951Z"

            val json = JsonObject()
            json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
            val data = JsonObject()
            data.addProperty("message_id", Integer.valueOf(messageId))
            data.addProperty("from", name)
            data.addProperty("from_user_name", name)
            data.addProperty("message", "Uploaded Image")
            data.addProperty("start_time", startTime)
            data.addProperty("file_path", path)
            data.addProperty("attachment_type", Integer.parseInt(TYPE_SECURE_IMAGE_UPLOAD))
            json.add("data", data)
            return json
        }

}

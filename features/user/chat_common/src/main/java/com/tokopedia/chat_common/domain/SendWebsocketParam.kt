package com.tokopedia.chat_common.domain

import com.google.gson.JsonObject
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING

/**
 * @author by nisie on 19/12/18.
 */
object SendWebsocketParam {

    fun generateParamSendMessage(messageId: String, sendMessage: String, startTime: String, toUid
    : String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", sendMessage)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)
        json.add("data", data)
        return json
    }

    fun generateParamSendProductAttachment(messageId: String,
                                           product: ResultProduct,
                                           startTime: String,
                                           toUid : String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", product.productUrl)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)
        data.addProperty("attachment_type", Integer.parseInt(TYPE_PRODUCT_ATTACHMENT))
        data.addProperty("product_id", product.productId)

        val productProfile = JsonObject()
        productProfile.addProperty("name", product.name)
        productProfile.addProperty("price", product.price)
        productProfile.addProperty("image_url", product.productImageThumbnail)
        productProfile.addProperty("url", product.productUrl)
        data.add("product_profile", productProfile)
        json.add("data", data)
        return json
    }


    fun generateParamSendImage(messageId: String, path: String, startTime: String, toUid : String):
            JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", "Uploaded Image")
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty("file_path", path)
        data.addProperty("attachment_type", Integer.parseInt(TYPE_IMAGE_UPLOAD))
        json.add("data", data)
        return json
    }

    fun getReadMessage(messageId: String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(messageId))
        json.add("data", data)
        return json
    }

    fun getParamStartTyping(messageId: String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(messageId))
        json.add("data", data)
        return json
    }

    fun getParamStopTyping(messageId: String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_END_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", Integer.valueOf(messageId))
        json.add("data", data)
        return json
    }


}
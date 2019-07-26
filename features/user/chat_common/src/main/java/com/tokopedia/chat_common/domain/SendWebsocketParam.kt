package com.tokopedia.chat_common.domain

import com.google.gson.JsonObject
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICE_SEND
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
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

    fun generateParamSendInvoiceAttachment(messageId: String,
//                                           product: ResultProduct,
                                           startTime: String,
                                           toUid : String): JsonObject {

        val attributes = JsonObject()
        attributes.addProperty("id", 329580570)
        attributes.addProperty("code", "INV/20190618/XIX/VI/329541479")
        attributes.addProperty("title", "Minions - Putih 36")
        attributes.addProperty("create_time", "18 Jun 2019")
        attributes.addProperty("image_url", "https://imagerouter.tokopedia.com/image/v1/p/471823361/product_s_thumbnail/desktop")
        attributes.addProperty("href_url", "https://www.tokopedia.com/invoice.pl?id=329580570&pdf=Invoice-7977933-956167-20190618122120-UkNUQ1BYTFQ")
        attributes.addProperty("status_id", 0)
        attributes.addProperty("status", "Pesanan Dibatalkan")
        attributes.addProperty("total_amount", "Rp 269.672")
//        attributes.addProperty("color", "red")

        val payload = JsonObject()
        payload.addProperty("type_id", 1)
        payload.addProperty("type", "marketplace")
        payload.add("attributes", attributes)
//        payload.addProperty("image_url", product.productImageThumbnail)
//        payload.addProperty("url", product.productUrl)

        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", "Halo")
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)
        data.addProperty("attachment_type", Integer.parseInt(TYPE_INVOICE_SEND))
        data.addProperty("source", "inbox")
        data.add("payload", payload)

        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
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
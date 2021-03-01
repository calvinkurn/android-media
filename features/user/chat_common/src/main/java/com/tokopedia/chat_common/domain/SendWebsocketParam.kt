package com.tokopedia.chat_common.domain

import com.google.gson.JsonObject
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICE_SEND
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_TYPING
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.view.viewmodel.InvoiceViewModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero

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

    fun generateParamSendProductAttachment(
            messageId: String,
            product: ResultProduct,
            startTime: String,
            toUid: String,
            productPreview: ProductPreview,
            message: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", product.productUrl)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)
        data.addProperty("attachment_type", Integer.parseInt(TYPE_PRODUCT_ATTACHMENT))
        data.addProperty("product_id", product.productId.toLongOrZero())

        val productProfile = JsonObject()
        productProfile.addProperty("name", product.name)
        productProfile.addProperty("price", product.price)
        productProfile.addProperty("price_before", productPreview.priceBefore)
        productProfile.addProperty("price_before_int", productPreview.priceBeforeInt)
        productProfile.addProperty("drop_percentage", productPreview.dropPercentage)
        productProfile.addProperty("image_url", product.productImageThumbnail)
        productProfile.addProperty("url", product.productUrl)
        productProfile.addProperty("text", message)
        productProfile.addProperty("status", productPreview.status)
        productProfile.add("variant", productPreview.generateVariantRequest())

        val freeShipping = JsonObject()
        freeShipping.addProperty("is_active", productPreview.productFsIsActive)
        freeShipping.addProperty("image_url", productPreview.productFsImageUrl)
        productProfile.add("free_ongkir", freeShipping)

        data.add("product_profile", productProfile)
        json.add("data", data)
        return json
    }

    fun generateParamSendInvoiceAttachment(messageId: String,
                                           invoice: InvoiceViewModel,
                                           startTime: String,
                                           toUid: String): JsonObject {

        val attributes = JsonObject()
        attributes.addProperty("id", invoice.id)
        attributes.addProperty("code", invoice.invoiceCode)
        attributes.addProperty("title", invoice.productName)
        attributes.addProperty("create_time", invoice.date)
        attributes.addProperty("image_url", invoice.imageUrl)
        attributes.addProperty("href_url", invoice.invoiceUrl)
        attributes.addProperty("status_id", invoice.statusId)
        attributes.addProperty("status", invoice.status)
        attributes.addProperty("total_amount", invoice.totalPriceAmount)

        val payload = JsonObject()
        payload.addProperty("type_id", 1)
        payload.addProperty("type", "marketplace")
        payload.add("attributes", attributes)

        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", invoice.invoiceUrl)
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


    fun generateParamSendImage(messageId: String, path: String, startTime: String, toUid: String):
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
        data.addProperty("no_update", true)
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
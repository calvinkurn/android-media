package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.common.InboxChatConstant.UPLOADING

/**
 * @author : Steven 01/01/19
 */

object TopChatWebSocketParam {

    fun generateParamSendMessage(
        thisMessageId: String,
        messageText: String,
        startTime: String,
        attachments: List<SendablePreview>,
        intention: String? = null,
        userLocationInfo: LocalCacheModel? = null
    ): String {
        val json = JsonObject().apply {
            addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        }
        val data = JsonObject().apply {
            addProperty("message_id", thisMessageId.toIntOrZero())
            addProperty("message", messageText)
            addProperty("source", "inbox")
            addProperty("start_time", startTime)
            if (attachments.isNotEmpty()) {
                val extras = createMessageExtrasAttachments(
                    attachments, intention, userLocationInfo
                )
                add("extras", extras)
            }
        }
        json.add("data", data)
        return json.toString()
    }

    fun generateParamSendProductAttachment(
        messageId: String,
        product: ResultProduct,
        startTime: String,
        toUid: String,
        productPreview: ProductPreview,
        message: String,
        userLocationInfo: LocalCacheModel
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", Integer.valueOf(messageId))
        data.addProperty("message", product.productUrl)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)
        data.addProperty(
            "attachment_type",
            Integer.parseInt(AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT)
        )
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

        val extras = createProductExtrasAttachments(userLocationInfo)
        data.add("extras", extras)

        data.add("product_profile", productProfile)
        json.add("data", data)
        return json
    }

    private fun createProductExtrasAttachments(
        userLocationInfo: LocalCacheModel
    ): JsonElement {
        val jsonObject = JsonObject()
        applyExtrasLocationStockTo(jsonObject, userLocationInfo)
        return jsonObject
    }

    private fun createMessageExtrasAttachments(
        attachments: List<SendablePreview>,
        intention: String?,
        userLocationInfo: LocalCacheModel?
    ): JsonElement {
        val jsonObject = JsonObject()
        applyExtrasProductTo(jsonObject, attachments)
        applyExtrasIntentionTo(jsonObject, intention)
        applyExtrasLocationStockTo(jsonObject, userLocationInfo)
        return jsonObject
    }

    private fun applyExtrasProductTo(
        jsonObject: JsonObject, attachments: List<SendablePreview>
    ) {
        val extrasProducts = JsonArray()
        attachments.forEach { attachment ->
            if (attachment is SendableProductPreview) {
                val product = JsonObject().apply {
                    addProperty("url", attachment.productUrl)
                    addProperty("product_id", attachment.productId.toLongOrZero())
                }
                extrasProducts.add(product)
            }
        }
        jsonObject.add("extras_product", extrasProducts)
    }

    private fun applyExtrasIntentionTo(
        jsonObject: JsonObject, intention: String?
    ) {
        intention?.let {
            jsonObject.addProperty("intent", it)
        }
    }

    private fun applyExtrasLocationStockTo(
        jsonObject: JsonObject, userLocationInfo: LocalCacheModel?
    ) {
        userLocationInfo?.let {
            val locationStock = createLocationStockProperty(it)
            jsonObject.add("location_stock", locationStock)
        }
    }

    private fun createLocationStockProperty(
        userLocationInfo: LocalCacheModel
    ): JsonObject {
        return JsonObject().apply {
            val latlon = "${userLocationInfo.lat},${userLocationInfo.long}"
            addProperty("address_id", userLocationInfo.address_id.toLongOrZero())
            addProperty("district_id", userLocationInfo.district_id.toLongOrZero())
            addProperty("postal_code", userLocationInfo.postal_code)
            addProperty("latlon", latlon)
        }
    }

    fun generateParamSendImage(thisMessageId: String, path: String, startTime: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", thisMessageId.toIntOrZero())
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
        data.addProperty("msg_id", thisMessageId.toIntOrZero())
        json.add("data", data)
        return json.toString()
    }

    fun generateParamStopTyping(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", thisMessageId.toIntOrZero())
        json.add("data", data)
        return json.toString()
    }

    fun generateParamRead(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject()
        data.addProperty("msg_id", thisMessageId.toIntOrZero())
        json.add("data", data)
        return json.toString()
    }

    fun generateParamCopyVoucherCode(
        thisMessageId: String,
        replyId: String,
        blastId: String,
        attachmentId: String,
        replyTime: String?,
        fromUid: String?
    ): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_COPY_VOUCHER_CODE)
        val data = JsonObject()
        data.addProperty("type", 1)
        val markReadBlast = JsonObject()
        markReadBlast.addProperty("msg_id", thisMessageId.toLongOrZero())
        markReadBlast.addProperty("reply_id", replyId.toLongOrZero())
        markReadBlast.addProperty("blast_id", blastId.toLongOrZero())
        markReadBlast.addProperty("attachment_id", attachmentId.toLongOrZero())
        markReadBlast.addProperty("user_id", fromUid.toLongOrZero())
        markReadBlast.addProperty("reply_time_nano", replyTime.toLongOrZero())
        data.add("mark_read_blast", markReadBlast)
        json.add("data", data)
        return json.toString()
    }

}

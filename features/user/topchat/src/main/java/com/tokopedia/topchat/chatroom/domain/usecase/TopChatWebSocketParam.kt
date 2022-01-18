package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.data.parentreply.ParentReplyWsRequest
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.common.InboxChatConstant.UPLOADING
import com.tokopedia.topchat.common.util.AddressUtil

/**
 * @author : Steven 01/01/19
 */

object TopChatWebSocketParam {

    private val gson = GsonBuilder().create()

    fun generateParamSendMessage(
        roomeMetaData: RoomMetaData,
        messageText: String,
        startTime: String,
        attachments: List<SendablePreview>,
        localId: String,
        intention: String? = null,
        userLocationInfo: LocalCacheModel? = null,
        referredMsg: ParentReply? = null
    ): String {
        val referredMsgRequest = generateParentReplyRequestPayload(referredMsg)
        val json = JsonObject().apply {
            addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        }
        val data = JsonObject().apply {
            addProperty("local_id", localId)
            addProperty("message_id", roomeMetaData.msgId.toLongOrZero())
            addProperty("message", messageText)
            addProperty("source", "inbox")
            addProperty("start_time", startTime)
            if (attachments.isNotEmpty()) {
                val extras = createMessageExtrasAttachments(
                    attachments, intention, userLocationInfo
                )
                add("extras", extras)
            }
            if (referredMsg != null) {
                add("parent_reply", referredMsgRequest)
            }
        }
        json.add("data", data)
        return json.toString()
    }

    fun generateParentReplyRequestPayload(
        parentReply: ParentReply?
    ): JsonElement? {
        if (parentReply == null) return null
        val requestContract = ParentReplyWsRequest(
            attachment_id = parentReply.attachmentId.toLongOrZero(),
            attachment_type = parentReply.attachmentType.toLongOrZero(),
            sender_id = parentReply.senderId.toLongOrZero() ,
            reply_time = parentReply.replyTime.toLongOrZero(),
            main_text = parentReply.mainText,
            sub_text = parentReply.subText,
            image_url = parentReply.imageUrl,
            local_id = parentReply.localId,
            source = "inbox"
        )
        return gson.toJsonTree(requestContract)
    }

    fun generateParamSendProductAttachment(
        messageId: String,
        product: ResultProduct,
        startTime: String,
        toUid: String,
        productPreview: ProductPreview,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", messageId.toLong())
        data.addProperty("local_id", localId)
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
        productProfile.addProperty("price_before_int", productPreview.priceBeforeInt.toLong())
        productProfile.addProperty("drop_percentage", productPreview.dropPercentage)
        productProfile.addProperty("image_url", product.productImageThumbnail)
        productProfile.addProperty("url", product.productUrl)
        productProfile.addProperty("text", message)
        productProfile.addProperty("status", productPreview.status)
        productProfile.addProperty("remaining_stock", productPreview.remainingStock)
        productProfile.addProperty("is_variant", productPreview.isSupportVariant)
        productProfile.add("variant", productPreview.generateVariantRequest())
        productProfile.addProperty("campaign_id", productPreview.campaignId)
        productProfile.addProperty("is_preorder", productPreview.isPreorder)
        productProfile.addProperty("price_int", productPreview.priceInt)
        productProfile.addProperty("category_id", productPreview.categoryId)

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
            val lat = userLocationInfo.lat
            val long = userLocationInfo.long
            val latlon = if (lat.isNotEmpty() && long.isNotEmpty()) {
                "$lat,$long"
            } else ""
            addProperty("address_id", userLocationInfo.address_id.toLongOrZero())
            addProperty("district_id", userLocationInfo.district_id.toLongOrZero())
            addProperty("postal_code", userLocationInfo.postal_code)
            addProperty(
                "address_name", AddressUtil.getAddressMasking(userLocationInfo.label)
            )
            addProperty("latlon", latlon)
        }
    }

    fun generateParamSendImage(
        thisMessageId: String,
        path: String,
        image: ImageUploadUiModel
    ): String {
        val referredMsgRequest = generateParentReplyRequestPayload(image.parentReply)
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("local_id", image.localId)
        data.addProperty("message_id", thisMessageId.toLongOrZero())
        data.addProperty("message", UPLOADING)
        data.addProperty("start_time", image.startTime)
        data.addProperty("file_path", path)
        data.addProperty("attachment_type", Integer.parseInt(TYPE_IMAGE_UPLOAD))
        if (referredMsgRequest != null) {
            data.add("parent_reply", referredMsgRequest)
        }
        json.add("data", data)
        return json.toString()
    }

    fun generateParamStartTyping(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", thisMessageId.toLongOrZero())
        json.add("data", data)
        return json.toString()
    }

    fun generateParamStopTyping(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_END_TYPING)
        val data = JsonObject()
        data.addProperty("msg_id", thisMessageId.toLongOrZero())
        json.add("data", data)
        return json.toString()
    }

    fun generateParamRead(thisMessageId: String): String {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject()
        data.addProperty("msg_id", thisMessageId.toLongOrZero())
        json.add("data", data)
        return json.toString()
    }

}

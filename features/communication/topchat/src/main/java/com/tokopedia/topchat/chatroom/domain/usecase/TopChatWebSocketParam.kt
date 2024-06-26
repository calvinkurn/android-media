package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD_SECURE
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.data.parentreply.ParentReplyWsRequest
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel
import com.tokopedia.topchat.common.util.AddressUtil

/**
 * @author : Steven 01/01/19
 */

object TopChatWebSocketParam {

    private val gson = GsonBuilder().create()
    private const val UPLOADING = "Uploaded Image"

    fun generateParamSendMessage(
        roomeMetaData: RoomMetaData,
        messageText: String,
        startTime: String,
        attachments: List<SendablePreview>,
        localId: String,
        intention: String? = null,
        userLocationInfo: LocalCacheModel? = null,
        referredMsg: ParentReply? = null,
        sourceReply: String
    ): String {
        val referredMsgRequest = generateParentReplyRequestPayload(referredMsg, sourceReply)
        val json = JsonObject().apply {
            addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        }
        val data = JsonObject().apply {
            addProperty("local_id", localId)
            addProperty("message_id", roomeMetaData.msgId.toLongOrZero())
            addProperty("message", messageText)
            addProperty("source", sourceReply)
            addProperty("start_time", startTime)
            if (attachments.isNotEmpty()) {
                val extras = createMessageExtrasAttachments(
                    attachments,
                    intention,
                    userLocationInfo
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
        parentReply: ParentReply?,
        sourceReply: String
    ): JsonElement? {
        if (parentReply == null) return null
        val requestContract = ParentReplyWsRequest(
            attachment_id = parentReply.attachmentId.toLongOrZero(),
            attachment_type = parentReply.attachmentType.toLongOrZero(),
            sender_id = parentReply.senderId.toLongOrZero(),
            reply_time = parentReply.replyTime.toLongOrZero(),
            main_text = parentReply.mainText,
            sub_text = parentReply.subText,
            image_url = parentReply.imageUrl,
            local_id = parentReply.localId,
            source = sourceReply
        )
        return gson.toJsonTree(requestContract)
    }

    fun generateParamSendProductAttachment(
        product: TopchatProductAttachmentPreviewUiModel,
        msgId: String,
        startTime: String,
        toUid: String,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String,
        sourceReply: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", msgId.toLongOrZero())
        data.addProperty("local_id", localId)
        data.addProperty("message", product.productUrl)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)
        data.addProperty("attachment_type", TYPE_PRODUCT_ATTACHMENT.toIntOrZero())
        data.addProperty("product_id", product.productId.toLongOrZero())

        val productProfile = createProductProfilePayload(product, message)
        val extras = createProductExtrasAttachments(userLocationInfo)

        data.add("extras", extras)
        data.add("product_profile", productProfile)
        data.addProperty("source", sourceReply)
        json.add("data", data)
        return json
    }

    private fun createProductProfilePayload(
        product: TopchatProductAttachmentPreviewUiModel,
        message: String
    ): JsonObject {
        val productProfile = JsonObject()
        productProfile.addProperty("name", product.productName)
        productProfile.addProperty("parent_id", product.parentId.toLongOrZero())
        productProfile.addProperty("price", product.productPrice)
        productProfile.addProperty("price_before", product.priceBefore)
        productProfile.addProperty("drop_percentage", product.dropPercentage)
        productProfile.addProperty("image_url", product.productImage)
        productProfile.addProperty("url", product.productUrl)
        productProfile.addProperty("android_url", product.androidUrl)
        productProfile.addProperty("ios_url", product.iosUrl)
        productProfile.addProperty("text", message)
        productProfile.addProperty("status", product.status)
        productProfile.addProperty("remaining_stock", product.remainingStock)
        productProfile.addProperty("is_variant", product.isSupportVariant)
        productProfile.add("variant", product.generateVariantRequest())
        productProfile.addProperty("campaign_id", product.campaignId.toLongOrZero())
        productProfile.addProperty("is_preorder", product.isPreOrder)
        productProfile.addProperty("price_int", product.priceNumber.toLong())
        productProfile.addProperty("category_id", product.categoryId.toLongOrZero())
        productProfile.addProperty("is_upcoming_campaign_product", product.isUpcomingCampaign)
        productProfile.addProperty("shop_id", product.shopId.toLongOrZero())
        productProfile.addProperty("min_order", product.minOrder)
        productProfile.addProperty("wishlist", product.wishList)
        productProfile.addProperty("is_fulfillment", product.isFulfillment)
        productProfile.addProperty("icon_tokocabang", product.urlTokocabang)
        productProfile.addProperty("desc_tokocabang", product.descTokoCabang)
        val locationStock = gson.toJsonTree(product.locationStock)
        productProfile.add("location_stock", locationStock)
        val images = gson.toJsonTree(product.images)
        productProfile.add("list_image_url", images)
        val rating = gson.toJsonTree(product.rating)
        productProfile.add("rating", rating)
        val playstoreProductData = gson.toJsonTree(product.playStoreData)
        productProfile.add("playstore_product_data", playstoreProductData)
        val freeShipping = gson.toJsonTree(product.freeShipping)
        productProfile.add("free_ongkir", freeShipping)
        return productProfile
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
        jsonObject: JsonObject,
        attachments: List<SendablePreview>
    ) {
        val extrasProducts = JsonArray()
        attachments.forEach { attachment ->
            if (attachment is TopchatProductAttachmentPreviewUiModel) {
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
        jsonObject: JsonObject,
        intention: String?
    ) {
        intention?.let {
            jsonObject.addProperty("intent", it)
        }
    }

    private fun applyExtrasLocationStockTo(
        jsonObject: JsonObject,
        userLocationInfo: LocalCacheModel?
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
            } else {
                ""
            }
            addProperty("address_id", userLocationInfo.address_id.toLongOrZero())
            addProperty("district_id", userLocationInfo.district_id.toLongOrZero())
            addProperty("postal_code", userLocationInfo.postal_code)
            addProperty(
                "address_name",
                AddressUtil.getAddressMasking(userLocationInfo.label)
            )
            addProperty("latlon", latlon)
        }
    }

    fun generateParamSendImage(
        thisMessageId: String,
        path: String,
        image: ImageUploadUiModel,
        isSecure: Boolean,
        sourceReply: String
    ): String {
        val referredMsgRequest = generateParentReplyRequestPayload(image.parentReply, sourceReply)
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("local_id", image.localId)
        data.addProperty("message_id", thisMessageId.toLongOrZero())
        data.addProperty("message", UPLOADING)
        data.addProperty("start_time", image.startTime)
        data.addProperty("file_path", path)
        val attachmentType = if (isSecure) {
            TYPE_IMAGE_UPLOAD_SECURE
        } else {
            TYPE_IMAGE_UPLOAD
        }
        data.addProperty("attachment_type", attachmentType.toIntOrZero())
        if (referredMsgRequest != null) {
            data.add("parent_reply", referredMsgRequest)
        }
        data.addProperty("source", sourceReply)
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

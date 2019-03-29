package com.tokopedia.chat_common.domain.mapper

import android.support.annotation.NonNull
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.imageupload.ImageUploadAttributes
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import javax.inject.Inject

/**
 * @author by nisie on 10/12/18.
 */
open class WebsocketMessageMapper @Inject constructor() {

    open fun map(pojo: ChatSocketPojo): Visitable<*> {

        return if (hasAttachment(pojo)) {
            val jsonAttributes = pojo.attachment!!.attributes
            mapAttachmentMessage(pojo, jsonAttributes!!)
        } else {
            convertToMessageViewModel(pojo)
        }


    }

    open fun convertToMessageViewModel(pojo: ChatSocketPojo): Visitable<*> {
        return MessageViewModel(pojo.msgId.toString(),
                pojo.fromUid,
                pojo.from,
                pojo.fromRole,
                "",
                "",
                pojo.message.timeStampUnixNano,
                pojo.startTime,
                pojo.message.censoredReply, false, false, !pojo.isOpposite)
    }

    open fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        return when (pojo.attachment!!.type) {
            TYPE_PRODUCT_ATTACHMENT -> convertToProductAttachment(pojo, jsonAttributes)
            TYPE_IMAGE_UPLOAD -> convertToImageUpload(pojo, jsonAttributes)
            else -> convertToFallBackModel(pojo)
        }
    }


    private fun convertToImageUpload(@NonNull pojo: ChatSocketPojo, jsonAttribute: JsonObject):
            ImageUploadViewModel {
        val pojoAttribute = GsonBuilder().create().fromJson<ImageUploadAttributes>(jsonAttribute,
                ImageUploadAttributes::class.java)

        return ImageUploadViewModel(
                pojo.msgId.toString(),
                pojo.fromUid,
                pojo.from,
                pojo.fromRole,
                pojo.attachment!!.id,
                pojo.attachment!!.type,
                pojo.message.timeStampUnixNano,
                !pojo.isOpposite,
                pojoAttribute.imageUrl,
                pojoAttribute.thumbnail,
                pojo.startTime,
                pojo.message.censoredReply
        )
    }


    private fun convertToProductAttachment(@NonNull pojo: ChatSocketPojo, jsonAttribute:
    JsonObject): ProductAttachmentViewModel {
        val pojoAttribute = GsonBuilder().create().fromJson<ProductAttachmentAttributes>(jsonAttribute,
                ProductAttachmentAttributes::class.java)

        return ProductAttachmentViewModel(
                pojo.msgId.toString(),
                pojo.fromUid,
                pojo.from,
                pojo.fromRole,
                pojo.attachment!!.id,
                pojo.attachment!!.type,
                pojo.message.timeStampUnixNano,
                pojoAttribute.productId,
                pojoAttribute.productProfile.name,
                pojoAttribute.productProfile.price,
                pojoAttribute.productProfile.url,
                pojoAttribute.productProfile.imageUrl,
                !pojo.isOpposite,
                pojo.message.censoredReply,
                pojo.startTime,
                canShowFooterProductAttachment(pojo.isOpposite,
                        pojo.fromRole),
                pojo.blastId,
                pojoAttribute.productProfile.priceInt,
                pojoAttribute.productProfile.category,
                pojoAttribute.productProfile.variant.toString(),
                pojoAttribute.productProfile.dropPercentage,
                pojoAttribute.productProfile.priceBefore,
                pojoAttribute.productProfile.shopId
        )
    }

    private fun canShowFooterProductAttachment(isOpposite: Boolean, role: String): Boolean {
        val ROLE_USER = "User"

        return (!isOpposite && role.toLowerCase() == ROLE_USER.toLowerCase())
                || (isOpposite && role.toLowerCase() != ROLE_USER.toLowerCase())
    }

    open fun convertToFallBackModel(pojo: ChatSocketPojo): Visitable<*> {
        var fallbackMessage = ""
        pojo.attachment?.fallbackAttachment?.let{
            fallbackMessage = it.message
        }
        return FallbackAttachmentViewModel(
                pojo.msgId.toString(),
                pojo.fromUid,
                pojo.from,
                pojo.fromRole,
                pojo.attachment!!.id,
                pojo.attachment!!.type,
                pojo.message.timeStampUnixNano,
                fallbackMessage
        )
    }

    open fun hasAttachment(pojo: ChatSocketPojo): Boolean {
        return (pojo.attachment != null
                && pojo.attachment?.attributes != null)
    }

}
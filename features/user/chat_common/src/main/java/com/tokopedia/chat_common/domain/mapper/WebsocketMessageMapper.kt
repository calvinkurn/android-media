package com.tokopedia.chat_common.domain.mapper

import androidx.annotation.NonNull
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICE_SEND
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.imageupload.ImageUploadAttributes
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceSentPojo
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
        return MessageViewModel(
                messageId = pojo.msgId.toString(),
                fromUid = pojo.fromUid,
                from = pojo.from,
                fromRole = pojo.fromRole,
                attachmentId = "",
                attachmentType = "",
                replyTime = pojo.message.timeStampUnixNano,
                startTime = pojo.startTime,
                message = pojo.message.censoredReply,
                isRead = false,
                isDummy = false,
                isSender = !pojo.isOpposite,
                source = pojo.source
        )
    }

    open fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        return when (pojo.attachment!!.type) {
            TYPE_PRODUCT_ATTACHMENT -> convertToProductAttachment(pojo, jsonAttributes)
            TYPE_IMAGE_UPLOAD -> convertToImageUpload(pojo, jsonAttributes)
            TYPE_INVOICE_SEND -> convertToInvoiceSent(pojo, jsonAttributes)
            else -> convertToFallBackModel(pojo)
        }
    }

    private fun convertToImageUpload(@NonNull pojo: ChatSocketPojo, jsonAttribute: JsonObject):
            ImageUploadViewModel {
        val pojoAttribute = GsonBuilder().create().fromJson<ImageUploadAttributes>(jsonAttribute,
                ImageUploadAttributes::class.java)

        return ImageUploadViewModel(
                messageId = pojo.msgId.toString(),
                fromUid = pojo.fromUid,
                from = pojo.from,
                fromRole = pojo.fromRole,
                attachmentId = pojo.attachment!!.id,
                attachmentType = pojo.attachment!!.type,
                replyTime = pojo.message.timeStampUnixNano,
                isSender = !pojo.isOpposite,
                imageUrl = pojoAttribute.imageUrl,
                imageUrlThumbnail = pojoAttribute.thumbnail,
                startTime = pojo.startTime,
                message = pojo.message.censoredReply,
                source = pojo.source
        )
    }

    private fun convertToProductAttachment(@NonNull pojo: ChatSocketPojo, jsonAttribute:
    JsonObject): ProductAttachmentViewModel {
        val pojoAttribute = GsonBuilder().create().fromJson<ProductAttachmentAttributes>(jsonAttribute,
                ProductAttachmentAttributes::class.java)

        val variant: List<AttachmentVariant> = pojoAttribute.productProfile.variant ?: emptyList()

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
                variant,
                pojoAttribute.productProfile.dropPercentage,
                pojoAttribute.productProfile.priceBefore,
                pojoAttribute.productProfile.shopId,
                pojoAttribute.productProfile.freeShipping,
                pojoAttribute.productProfile.categoryId,
                pojoAttribute.productProfile.playStoreData,
                pojoAttribute.productProfile.remainingStock,
                pojoAttribute.productProfile.status,
                pojo.source,
                pojoAttribute.productProfile.rating
        ).apply {
            finishLoading()
        }
    }

    private fun convertToInvoiceSent(pojo: ChatSocketPojo, jsonAttribute: JsonObject):
            AttachInvoiceSentViewModel {
        val invoiceSentPojo = GsonBuilder().create().fromJson(jsonAttribute,
                InvoiceSentPojo::class.java)
        return AttachInvoiceSentViewModel(
                msgId = pojo.msgId.toString(),
                fromUid = pojo.fromUid,
                from = pojo.from,
                fromRole = pojo.fromRole,
                attachmentId = pojo.attachment!!.id,
                attachmentType = pojo.attachment!!.type,
                replyTime = pojo.message.timeStampUnixNano,
                startTime = pojo.startTime,
                message = invoiceSentPojo.invoiceLink.attributes.title,
                description = invoiceSentPojo.invoiceLink.attributes.description,
                imageUrl = invoiceSentPojo.invoiceLink.attributes.imageUrl,
                totalAmount = invoiceSentPojo.invoiceLink.attributes.totalAmount,
                isSender = !pojo.isOpposite,
                statusId = invoiceSentPojo.invoiceLink.attributes.statusId,
                status = invoiceSentPojo.invoiceLink.attributes.status,
                invoiceId = invoiceSentPojo.invoiceLink.attributes.code,
                invoiceUrl = invoiceSentPojo.invoiceLink.attributes.hrefUrl,
                createTime = invoiceSentPojo.invoiceLink.attributes.createTime,
                source = pojo.source
        ).apply {
            finishLoading()
        }
    }

    private fun canShowFooterProductAttachment(isOpposite: Boolean, role: String): Boolean {
        val ROLE_USER = "User"

        return (!isOpposite && role.toLowerCase() == ROLE_USER.toLowerCase())
                || (isOpposite && role.toLowerCase() != ROLE_USER.toLowerCase())
    }

    open fun convertToFallBackModel(pojo: ChatSocketPojo): Visitable<*> {
        var fallbackMessage = ""
        pojo.attachment?.fallbackAttachment?.let {
            fallbackMessage = it.message
        }
        return FallbackAttachmentViewModel(
                msgId = pojo.msgId.toString(),
                fromUid = pojo.fromUid,
                from = pojo.from,
                fromRole = pojo.fromRole,
                attachmentId = pojo.attachment!!.id,
                attachmentType = pojo.attachment!!.type,
                replyTime = pojo.message.timeStampUnixNano,
                message = fallbackMessage,
                isOpposite = pojo.isOpposite,
                source = pojo.source
        )
    }

    open fun hasAttachment(pojo: ChatSocketPojo): Boolean {
        return (pojo.attachment != null
                && pojo.attachment?.attributes != null)
    }

}
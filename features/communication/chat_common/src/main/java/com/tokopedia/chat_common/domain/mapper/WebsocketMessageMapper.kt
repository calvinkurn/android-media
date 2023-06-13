package com.tokopedia.chat_common.domain.mapper

import androidx.annotation.NonNull
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD_SECURE
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICE_SEND
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.imageupload.ImageUploadAttributes
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceSentPojo
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import java.util.Locale
import javax.inject.Inject

/**
 * @author by nisie on 10/12/18.
 */
open class WebsocketMessageMapper @Inject constructor() {

    open fun map(pojo: ChatSocketPojo): Visitable<*> {
        pojo.generateLocalIdIfNotExist()
        return if (hasAttachment(pojo)) {
            val jsonAttributes = pojo.attachment!!.attributes
            mapAttachmentMessage(pojo, jsonAttributes!!)
        } else {
            convertToMessageViewModel(pojo)
        }
    }

    open fun convertToMessageViewModel(pojo: ChatSocketPojo): Visitable<*> {
        return MessageUiModel.Builder()
            .withResponseFromWs(pojo)
            .build()
    }

    open fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        return when (pojo.attachment!!.type) {
            TYPE_PRODUCT_ATTACHMENT -> convertToProductAttachment(pojo, jsonAttributes)
            TYPE_IMAGE_UPLOAD -> convertToImageUpload(pojo, jsonAttributes, TYPE_IMAGE_UPLOAD)
            TYPE_IMAGE_UPLOAD_SECURE ->
                convertToImageUpload(pojo, jsonAttributes, TYPE_IMAGE_UPLOAD_SECURE)
            TYPE_INVOICE_SEND -> convertToInvoiceSent(pojo, jsonAttributes)
            else -> convertToFallBackModel(pojo)
        }
    }

    private fun convertToImageUpload(
        @NonNull pojo: ChatSocketPojo,
        jsonAttribute: JsonObject,
        attachmentType: String
    ): ImageUploadUiModel {
        val pojoAttribute = GsonBuilder().create().fromJson(
            jsonAttribute, ImageUploadAttributes::class.java
        )
        return ImageUploadUiModel.Builder()
            .withResponseFromWs(pojo)
            .withAttachmentType(attachmentType)
            .withImageUrl(pojoAttribute.imageUrl)
            .withImageUrlThumbnail(pojoAttribute.thumbnail)
            .withImageSecureUrl(pojoAttribute.imageUrlSecure)
            .build()
    }

    private fun convertToProductAttachment(@NonNull pojo: ChatSocketPojo, jsonAttribute:
    JsonObject): ProductAttachmentUiModel {
        val canShowFooter = canShowFooterProductAttachment(pojo.isOpposite, pojo.fromRole)
        val pojoAttribute = GsonBuilder().create().fromJson(
            jsonAttribute, ProductAttachmentAttributes::class.java
        )
        return ProductAttachmentUiModel.Builder()
            .withResponseFromWs(pojo)
            .withProductAttributesResponse(pojoAttribute)
            .withCanShowFooter(canShowFooter)
            .withNeedSync(false)
            .build()
    }

    private fun convertToInvoiceSent(pojo: ChatSocketPojo, jsonAttribute: JsonObject):
            AttachInvoiceSentUiModel {
        val invoiceSentPojo = GsonBuilder().create().fromJson(jsonAttribute,
                InvoiceSentPojo::class.java)
        return AttachInvoiceSentUiModel.Builder()
            .withResponseFromWs(pojo)
            .withInvoiceAttributesResponse(invoiceSentPojo.invoiceLink)
            .withNeedSync(false)
            .build()
    }

    private fun canShowFooterProductAttachment(isOpposite: Boolean, role: String): Boolean {
        val ROLE_USER = "User"

        return (!isOpposite && role.lowercase(Locale.getDefault()) == ROLE_USER.lowercase(Locale.getDefault()))
                || (isOpposite && role.lowercase(Locale.getDefault()) != ROLE_USER.lowercase(Locale.getDefault()))
    }

    open fun convertToFallBackModel(pojo: ChatSocketPojo): Visitable<*> {
        var fallbackMessage = ""
        pojo.attachment?.fallbackAttachment?.let {
            fallbackMessage = it.message
        }
        return FallbackAttachmentUiModel.Builder()
            .withResponseFromWs(pojo)
            .withMsg(fallbackMessage)
            .build()
    }

    open fun hasAttachment(pojo: ChatSocketPojo): Boolean {
        return (pojo.attachment != null
                && pojo.attachment?.attributes != null)
    }

}
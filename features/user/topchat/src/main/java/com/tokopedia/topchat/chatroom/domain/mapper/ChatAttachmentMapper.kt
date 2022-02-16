package com.tokopedia.topchat.chatroom.domain.mapper

import androidx.collection.ArrayMap
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceSentPojo
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.preattach.PreAttachPayloadResponse
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewReminderAttribute
import javax.inject.Inject

class ChatAttachmentMapper @Inject constructor() {

    fun map(response: ChatAttachmentResponse): ArrayMap<String, Attachment> {
        return mapAttachments(response.chatAttachments.list)
    }

    fun map(response: PreAttachPayloadResponse): ArrayMap<String, Attachment> {
        return mapAttachments(response.chatPreAttachPayload.list)
    }

    private fun mapAttachments(attachments: List<Attachment>): ArrayMap<String, Attachment> {
        val map = ArrayMap<String, Attachment>()
        for (attachment in attachments) {
            parseAttribute(attachment)
            map[attachment.id] = attachment
        }
        return map
    }

    fun mapError(attachmentId: String): ArrayMap<String, Attachment> {
        val map = ArrayMap<String, Attachment>()
        val attachments = attachmentId.split(",")
        attachments.forEach {
            map[it.trim()] = ErrorAttachment()
        }
        return map
    }

    private fun parseAttribute(attachment: Attachment) {
        attachment.parsedAttributes = when (attachment.type) {
            AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT.toInt() -> {
                convertToProductAttachment(attachment)
            }
            AttachmentType.Companion.TYPE_INVOICE_SEND.toInt() -> {
                convertToInvoiceAttachment(attachment)
            }
            AttachmentType.Companion.TYPE_REVIEW_REMINDER.toInt() -> {
                convertToReviewReminderAttachment(attachment)
            }
            AttachmentType.Companion.TYPE_IMAGE_ANNOUNCEMENT.toInt() -> {
                convertToImageAnnouncementAttachment(attachment)
            }
            else -> null
        }
    }

    private fun convertToImageAnnouncementAttachment(
        attachment: Attachment
    ): ImageAnnouncementPojo {
        return CommonUtil.fromJson(attachment.attributes, ImageAnnouncementPojo::class.java)
    }

    private fun convertToInvoiceAttachment(attachment: Attachment): InvoiceSentPojo {
        return CommonUtil.fromJson(attachment.attributes, InvoiceSentPojo::class.java)
    }

    private fun convertToProductAttachment(attachment: Attachment): ProductAttachmentAttributes {
        return CommonUtil.fromJson(attachment.attributes, ProductAttachmentAttributes::class.java)
    }

    private fun convertToReviewReminderAttachment(attachment: Attachment): Any? {
        return CommonUtil.fromJson<ReviewReminderAttribute>(
                attachment.attributes,
                ReviewReminderAttribute::class.java
        )
    }

}
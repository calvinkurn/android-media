package com.tokopedia.chat_common.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_ANNOUNCEMENT
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICE_SEND
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
import com.tokopedia.chat_common.domain.pojo.Contact
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.chat_common.domain.pojo.imageupload.ImageUploadAttributes
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceSentPojo
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel
import javax.inject.Inject

/**
 * @author by nisie on 14/12/18.
 */
open class GetExistingChatMapper @Inject constructor() {

    protected var latestHeaderDate: String = ""
    protected val gson = GsonBuilder().create()

    open fun map(pojo: GetExistingChatPojo): ChatroomViewModel {
        val listChat = mappingListChat(pojo)
        val headerModel = mappingHeaderModel(pojo)
        val canLoadMore = pojo.chatReplies.hasNext
        val isReplyable: Boolean = pojo.chatReplies.textAreaReply != 0
        val blockedStatus: BlockedStatus = mapBlockedStatus(pojo)
        val replyIDs = pojo.chatReplies.replyIDs
        listChat.reverse()
        return ChatroomViewModel(
                listChat, headerModel,
                canLoadMore, isReplyable,
                blockedStatus, latestHeaderDate,
                replyIDs
        )

    }

    private fun mapBlockedStatus(pojo: GetExistingChatPojo): BlockedStatus {
        return BlockedStatus(pojo.chatReplies.block.isBlocked,
                pojo.chatReplies.block.isPromoBlocked,
                pojo.chatReplies.block.blockedUntil)
    }

    open fun mappingHeaderModel(pojo: GetExistingChatPojo): ChatRoomHeaderUiModel {
        var interlocutor = Contact()

        for (contact in pojo.chatReplies.contacts) {
            if (contact.isInterlocutor) {
                interlocutor = contact
                break
            }
        }

        return ChatRoomHeaderUiModel(
                interlocutor.name,
                interlocutor.tag,
                interlocutor.userId.toString(),
                interlocutor.role,
                ChatRoomHeaderUiModel.Companion.MODE_DEFAULT_GET_CHAT,
                "",
                interlocutor.thumbnail,
                interlocutor.status.timestampStr,
                interlocutor.status.isOnline,
                interlocutor.shopId,
                interlocutor.isOfficial,
                interlocutor.isGold,
                interlocutor.shopType,
                interlocutor.badge
        )
    }

    open fun mappingListChat(pojo: GetExistingChatPojo): ArrayList<Visitable<*>> {
        val listChat: ArrayList<Visitable<*>> = ArrayList()

        for (chatItemPojo in pojo.chatReplies.list) {
            val date = chatItemPojo.date
            for (chatItemPojoByDate in chatItemPojo.chats) {
                val time = chatItemPojoByDate.time
                for (chatItemPojoByDateByTime in chatItemPojoByDate.replies) {
                    if (hasAttachment(chatItemPojoByDateByTime)) {
                        listChat.add(mapAttachment(chatItemPojoByDateByTime))
                    } else {
                        listChat.add(convertToMessageViewModel(chatItemPojoByDateByTime)
                        )
                    }
                }
            }
        }
        return listChat
    }

    open fun convertToMessageViewModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return MessageUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
            .build()
    }

    open fun mapAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return when (chatItemPojoByDateByTime.attachment?.type.toString()) {
            TYPE_PRODUCT_ATTACHMENT -> convertToProductAttachment(chatItemPojoByDateByTime)
            TYPE_IMAGE_UPLOAD -> convertToImageUpload(chatItemPojoByDateByTime)
            TYPE_IMAGE_ANNOUNCEMENT -> convertToImageAnnouncement(chatItemPojoByDateByTime)
            TYPE_INVOICE_SEND -> convertToInvoiceSent(chatItemPojoByDateByTime)
            else -> convertToFallBackModel(chatItemPojoByDateByTime)
        }
    }

    protected open fun convertToImageAnnouncement(item: Reply): Visitable<*> {
        val pojoAttribute = gson.fromJson<ImageAnnouncementPojo>(item.attachment?.attributes,
                ImageAnnouncementPojo::class.java)
        val imageAnnouncement = ImageAnnouncementUiModel(
                messageId = item.msgId.toString(),
                fromUid = item.senderId.toString(),
                from = item.senderName,
                fromRole = item.role,
                attachmentId = item.attachment?.id ?: "",
                attachmentType = item.attachment?.type.toString(),
                replyTime = item.replyTime,
                imageUrl = pojoAttribute.imageUrl,
                redirectUrl = pojoAttribute.url,
                isHideBanner = pojoAttribute.isHideBanner,
                message = item.msg,
                broadcastBlastId = item.blastId,
                source = item.source
        )
        return imageAnnouncement
    }

    private fun convertToFallBackModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        var fallbackMessage = ""
        chatItemPojoByDateByTime.attachment?.fallback?.let {
            fallbackMessage = it.message
        }
        return FallbackAttachmentUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
            .withMsg(fallbackMessage)
            .build()
    }

    private fun convertToImageUpload(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val pojoAttribute = gson.fromJson(
            chatItemPojoByDateByTime.attachment?.attributes,
            ImageUploadAttributes::class.java
        )
        return ImageUploadUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
            .withImageUrl(pojoAttribute.imageUrl)
            .withImageUrlThumbnail(pojoAttribute.thumbnail)
            .build()
    }

    open fun convertToProductAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val pojoAttribute = gson.fromJson(
            chatItemPojoByDateByTime.attachment?.attributes,
            ProductAttachmentAttributes::class.java
        )
        val canShowFooter = canShowFooterProductAttachment(
            chatItemPojoByDateByTime.isOpposite, chatItemPojoByDateByTime.role
        )
        if (pojoAttribute.isBannedProduct()) {
            return BannedProductAttachmentUiModel.Builder()
                .withResponseFromGQL(chatItemPojoByDateByTime)
                .withProductAttributesResponse(pojoAttribute)
                .withCanShowFooter(canShowFooter)
                .build()
        }
        return ProductAttachmentUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
            .withProductAttributesResponse(pojoAttribute)
            .withCanShowFooter(canShowFooter)
            .build()
    }

    private fun convertToInvoiceSent(pojo: Reply): AttachInvoiceSentUiModel {
        val invoiceAttributes = pojo.attachment?.attributes
        val invoiceSentPojo = gson.fromJson(invoiceAttributes, InvoiceSentPojo::class.java)
        return AttachInvoiceSentUiModel.Builder()
            .withResponseFromGQL(pojo)
            .withInvoiceAttributesResponse(invoiceSentPojo.invoiceLink)
            .build()
    }

    private fun canShowFooterProductAttachment(isOpposite: Boolean, role: String): Boolean {
        val ROLE_USER = "User"

        return (!isOpposite && role.toLowerCase() == ROLE_USER.toLowerCase())
                || (isOpposite && role.toLowerCase() != ROLE_USER.toLowerCase())
    }

    open fun hasAttachment(pojo: Reply): Boolean {
        return (pojo.attachment?.type != null && pojo.attachment.attributes.isNotBlank())
    }
}
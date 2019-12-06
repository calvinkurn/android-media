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
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import javax.inject.Inject

/**
 * @author by nisie on 14/12/18.
 */
open class GetExistingChatMapper @Inject constructor() {

    open fun map(pojo: GetExistingChatPojo): ChatroomViewModel {

        val listChat = mappingListChat(pojo)
        val headerModel = mappingHeaderModel(pojo)
        val canLoadMore = pojo.chatReplies.hasNext
        val isReplyable: Boolean = pojo.chatReplies.textAreaReply != 0
        val blockedStatus: BlockedStatus = mapBlockedStatus(pojo)
        listChat.reverse()
        return ChatroomViewModel(listChat, headerModel, canLoadMore, isReplyable, blockedStatus)

    }

    private fun mapBlockedStatus(pojo: GetExistingChatPojo): BlockedStatus {
        return BlockedStatus(pojo.chatReplies.block.isBlocked,
                pojo.chatReplies.block.isPromoBlocked,
                pojo.chatReplies.block.blockedUntil)
    }

    open fun mappingHeaderModel(pojo: GetExistingChatPojo): ChatRoomHeaderViewModel {
        var interlocutor = Contact()

        for (contact in pojo.chatReplies.contacts) {
            if (contact.isInterlocutor) {
                interlocutor = contact
                break
            }
        }

        return ChatRoomHeaderViewModel(
                interlocutor.name,
                interlocutor.tag,
                interlocutor.userId.toString(),
                interlocutor.role,
                ChatRoomHeaderViewModel.Companion.MODE_DEFAULT_GET_CHAT,
                "",
                interlocutor.thumbnail,
                interlocutor.status.timestampStr,
                interlocutor.status.isOnline,
                interlocutor.shopId
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

    private fun convertToMessageViewModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return MessageViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment?.id ?: "",
                chatItemPojoByDateByTime.attachment?.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                "",
                chatItemPojoByDateByTime.isRead,
                false,
                !chatItemPojoByDateByTime.isOpposite,
                chatItemPojoByDateByTime.msg
        )
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

    private fun convertToImageAnnouncement(item: Reply): Visitable<*> {
        val pojoAttribute = GsonBuilder().create().fromJson<ImageAnnouncementPojo>(item.attachment?.attributes,
                ImageAnnouncementPojo::class.java)
        val imageAnnouncement = ImageAnnouncementViewModel(
                item.msgId.toString(),
                item.senderId.toString(),
                item.senderName,
                item.role,
                item.attachment?.id ?: "",
                item.attachment?.type.toString(),
                item.replyTime,
                pojoAttribute.imageUrl,
                pojoAttribute.url,
                item.msg,
                item.blastId
        )
        return imageAnnouncement
    }

    private fun convertToFallBackModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        var fallbackMessage = ""
        chatItemPojoByDateByTime.attachment?.fallback?.let {
            fallbackMessage = it.message
        }
        return FallbackAttachmentViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment?.id ?: "",
                chatItemPojoByDateByTime.attachment?.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                fallbackMessage,
                chatItemPojoByDateByTime.isOpposite
        )
    }

    private fun convertToImageUpload(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val pojoAttribute = GsonBuilder().create().fromJson<ImageUploadAttributes>(chatItemPojoByDateByTime.attachment?.attributes,
                ImageUploadAttributes::class.java)
        return ImageUploadViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment?.id ?: "",
                chatItemPojoByDateByTime.attachment?.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                !chatItemPojoByDateByTime.isOpposite,
                pojoAttribute.imageUrl,
                pojoAttribute.thumbnail,
                chatItemPojoByDateByTime.isRead,
                chatItemPojoByDateByTime.msg
        )
    }

    private fun convertToProductAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {

        val pojoAttribute = GsonBuilder().create().fromJson<ProductAttachmentAttributes>(chatItemPojoByDateByTime.attachment?.attributes,
                ProductAttachmentAttributes::class.java)

        val variant: List<AttachmentVariant> = if (pojoAttribute.productProfile.variant == null) {
            emptyList()
        } else {
            pojoAttribute.productProfile.variant
        }

        if (pojoAttribute.isBannedProduct()) {
            return BannedProductAttachmentViewModel(
                    chatItemPojoByDateByTime.msgId.toString(),
                    chatItemPojoByDateByTime.senderId.toString(),
                    chatItemPojoByDateByTime.senderName,
                    chatItemPojoByDateByTime.role,
                    chatItemPojoByDateByTime.attachment?.id ?: "",
                    chatItemPojoByDateByTime.attachment?.type.toString(),
                    chatItemPojoByDateByTime.replyTime,
                    chatItemPojoByDateByTime.isRead,
                    pojoAttribute.productId,
                    pojoAttribute.productProfile.name,
                    pojoAttribute.productProfile.price,
                    pojoAttribute.productProfile.url,
                    pojoAttribute.productProfile.imageUrl,
                    !chatItemPojoByDateByTime.isOpposite,
                    chatItemPojoByDateByTime.msg,
                    canShowFooterProductAttachment(chatItemPojoByDateByTime.isOpposite,
                            chatItemPojoByDateByTime.role),
                    chatItemPojoByDateByTime.blastId,
                    pojoAttribute.productProfile.priceInt,
                    pojoAttribute.productProfile.category,
                    variant,
                    pojoAttribute.productProfile.dropPercentage,
                    pojoAttribute.productProfile.priceBefore,
                    pojoAttribute.productProfile.shopId,
                    pojoAttribute.productProfile.freeShipping,
                    pojoAttribute.productProfile.categoryId,
                    pojoAttribute.productProfile.playStoreData,
                    pojoAttribute.productProfile.minOrder
            )
        }

        return ProductAttachmentViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment?.id ?: "",
                chatItemPojoByDateByTime.attachment?.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                chatItemPojoByDateByTime.isRead,
                pojoAttribute.productId,
                pojoAttribute.productProfile.name,
                pojoAttribute.productProfile.price,
                pojoAttribute.productProfile.url,
                pojoAttribute.productProfile.imageUrl,
                !chatItemPojoByDateByTime.isOpposite,
                chatItemPojoByDateByTime.msg,
                canShowFooterProductAttachment(chatItemPojoByDateByTime.isOpposite,
                        chatItemPojoByDateByTime.role),
                chatItemPojoByDateByTime.blastId,
                pojoAttribute.productProfile.priceInt,
                pojoAttribute.productProfile.category,
                variant,
                pojoAttribute.productProfile.dropPercentage,
                pojoAttribute.productProfile.priceBefore,
                pojoAttribute.productProfile.shopId,
                pojoAttribute.productProfile.freeShipping,
                pojoAttribute.productProfile.categoryId,
                pojoAttribute.productProfile.playStoreData,
                pojoAttribute.productProfile.minOrder
        )
    }

    private fun convertToInvoiceSent(pojo: Reply): AttachInvoiceSentViewModel {
        val invoiceAttributes = pojo.attachment?.attributes
        val invoiceSentPojo = GsonBuilder().create().fromJson(invoiceAttributes, InvoiceSentPojo::class.java)
        return AttachInvoiceSentViewModel(
                pojo.msgId.toString(),
                pojo.senderId.toString(),
                pojo.senderName,
                pojo.role,
                pojo.attachment?.id ?: "",
                pojo.attachment?.type.toString(),
                pojo.replyTime,
                invoiceSentPojo.invoiceLink.attributes.title,
                invoiceSentPojo.invoiceLink.attributes.description,
                invoiceSentPojo.invoiceLink.attributes.imageUrl,
                invoiceSentPojo.invoiceLink.attributes.totalAmount,
                !pojo.isOpposite,
                pojo.isRead,
                invoiceSentPojo.invoiceLink.attributes.statusId,
                invoiceSentPojo.invoiceLink.attributes.status,
                invoiceSentPojo.invoiceLink.attributes.code,
                invoiceSentPojo.invoiceLink.attributes.hrefUrl
        )

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
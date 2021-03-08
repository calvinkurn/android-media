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

    protected var latestHeaderDate: String = ""
    protected val gson = GsonBuilder().create()

    open fun map(pojo: GetExistingChatPojo): ChatroomViewModel {
        val listChat = mappingListChat(pojo)
        val headerModel = mappingHeaderModel(pojo)
        val canLoadMore = pojo.chatReplies.hasNext
        val isReplyable: Boolean = pojo.chatReplies.textAreaReply != 0
        val blockedStatus: BlockedStatus = mapBlockedStatus(pojo)
        val attachmentIds = pojo.chatReplies.attachmentIds
        listChat.reverse()
        return ChatroomViewModel(
                listChat, headerModel,
                canLoadMore, isReplyable,
                blockedStatus, latestHeaderDate,
                attachmentIds
        )

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
                interlocutor.shopId,
                interlocutor.isOfficial,
                interlocutor.isGold,
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
                chatItemPojoByDateByTime.msg,
                chatItemPojoByDateByTime.source,
                blastId = chatItemPojoByDateByTime.blastId
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
        val pojoAttribute = gson.fromJson<ImageAnnouncementPojo>(item.attachment?.attributes,
                ImageAnnouncementPojo::class.java)
        val imageAnnouncement = ImageAnnouncementViewModel(
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
                blastId = item.blastId,
                source = item.source
        )
        return imageAnnouncement
    }

    private fun convertToFallBackModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        var fallbackMessage = ""
        chatItemPojoByDateByTime.attachment?.fallback?.let {
            fallbackMessage = it.message
        }
        return FallbackAttachmentViewModel(
                msgId = chatItemPojoByDateByTime.msgId.toString(),
                fromUid = chatItemPojoByDateByTime.senderId.toString(),
                from = chatItemPojoByDateByTime.senderName,
                fromRole = chatItemPojoByDateByTime.role,
                attachmentId = chatItemPojoByDateByTime.attachment?.id ?: "",
                attachmentType = chatItemPojoByDateByTime.attachment?.type.toString(),
                replyTime = chatItemPojoByDateByTime.replyTime,
                message = fallbackMessage,
                isOpposite = chatItemPojoByDateByTime.isOpposite,
                source = chatItemPojoByDateByTime.source
        )
    }

    private fun convertToImageUpload(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val pojoAttribute = gson.fromJson<ImageUploadAttributes>(chatItemPojoByDateByTime.attachment?.attributes,
                ImageUploadAttributes::class.java)
        return ImageUploadViewModel(
                messageId = chatItemPojoByDateByTime.msgId.toString(),
                fromUid = chatItemPojoByDateByTime.senderId.toString(),
                from = chatItemPojoByDateByTime.senderName,
                fromRole = chatItemPojoByDateByTime.role,
                attachmentId = chatItemPojoByDateByTime.attachment?.id ?: "",
                attachmentType = chatItemPojoByDateByTime.attachment?.type.toString(),
                replyTime = chatItemPojoByDateByTime.replyTime,
                isSender = !chatItemPojoByDateByTime.isOpposite,
                imageUrl = pojoAttribute.imageUrl,
                imageUrlThumbnail = pojoAttribute.thumbnail,
                isRead = chatItemPojoByDateByTime.isRead,
                message = chatItemPojoByDateByTime.msg,
                source = chatItemPojoByDateByTime.source
        )
    }

    open fun convertToProductAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {

        val pojoAttribute = gson.fromJson<ProductAttachmentAttributes>(chatItemPojoByDateByTime.attachment?.attributes,
                ProductAttachmentAttributes::class.java)

        val variant: List<AttachmentVariant> = pojoAttribute.productProfile.variant ?: emptyList()

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
                    pojoAttribute.productProfile.minOrder,
                    pojoAttribute.productProfile.remainingStock,
                    pojoAttribute.productProfile.status,
                    pojoAttribute.productProfile.wishList,
                    pojoAttribute.productProfile.images,
                    chatItemPojoByDateByTime.source,
                    pojoAttribute.productProfile.rating,
                    chatItemPojoByDateByTime.replyId
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
                pojoAttribute.productProfile.minOrder,
                pojoAttribute.productProfile.remainingStock,
                pojoAttribute.productProfile.status,
                pojoAttribute.productProfile.wishList,
                pojoAttribute.productProfile.images,
                chatItemPojoByDateByTime.source,
                pojoAttribute.productProfile.rating,
                chatItemPojoByDateByTime.replyId
        )
    }

    private fun convertToInvoiceSent(pojo: Reply): AttachInvoiceSentViewModel {
        val invoiceAttributes = pojo.attachment?.attributes
        val invoiceSentPojo = gson.fromJson(invoiceAttributes, InvoiceSentPojo::class.java)
        return AttachInvoiceSentViewModel(
                msgId = pojo.msgId.toString(),
                fromUid = pojo.senderId.toString(),
                from = pojo.senderName,
                fromRole = pojo.role,
                attachmentId = pojo.attachment?.id ?: "",
                attachmentType = pojo.attachment?.type.toString(),
                replyTime = pojo.replyTime,
                message = invoiceSentPojo.invoiceLink.attributes.title,
                description = invoiceSentPojo.invoiceLink.attributes.description,
                imageUrl = invoiceSentPojo.invoiceLink.attributes.imageUrl,
                totalAmount = invoiceSentPojo.invoiceLink.attributes.totalAmount,
                isSender = !pojo.isOpposite,
                isRead = pojo.isRead,
                statusId = invoiceSentPojo.invoiceLink.attributes.statusId,
                status = invoiceSentPojo.invoiceLink.attributes.status,
                invoiceId = invoiceSentPojo.invoiceLink.attributes.code,
                invoiceUrl = invoiceSentPojo.invoiceLink.attributes.hrefUrl,
                createTime = invoiceSentPojo.invoiceLink.attributes.createTime,
                source = pojo.source
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
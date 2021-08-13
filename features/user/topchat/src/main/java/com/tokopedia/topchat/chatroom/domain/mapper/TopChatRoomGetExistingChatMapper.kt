package com.tokopedia.topchat.chatroom.domain.mapper

import androidx.collection.ArrayMap
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_CAROUSEL
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_DUAL_ANNOUNCEMENT
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUOTATION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_REVIEW_REMINDER
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_STICKER
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.ChatRepliesItem
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.domain.pojo.ImageDualAnnouncementPojo
import com.tokopedia.topchat.chatroom.domain.pojo.QuotationAttributes
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatVoucherPojo
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewReminderAttribute
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerAttributesResponse
import com.tokopedia.topchat.chatroom.view.uimodel.*
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel
import javax.inject.Inject

/**
 * @author : Steven 02/01/19
 */

open class TopChatRoomGetExistingChatMapper @Inject constructor() : GetExistingChatMapper() {

    override fun mappingListChat(pojo: GetExistingChatPojo): ArrayList<Visitable<*>> {
        val listChat: ArrayList<Visitable<*>> = ArrayList()
        val replies = pojo.chatReplies.list
        for ((index, chatItemPojo) in replies.withIndex()) {
            listChat.add(createHeaderDate(chatItemPojo))
            if (index == replies.lastIndex) {
                latestHeaderDate = chatItemPojo.date
            }
            for (chatItemPojoByDate in chatItemPojo.chats) {
                var replyIndex = 0
                while (replyIndex < chatItemPojoByDate.replies.size) {
                    val chatDateTime = chatItemPojoByDate.replies[replyIndex]
                    val nextItem = chatItemPojoByDate.replies.getOrNull(replyIndex + 1)
                    when {
                        // Merge broadcast bubble
                        chatDateTime.isBroadCast() &&
                                chatDateTime.isAlsoTheSameBroadcast(nextItem) -> {
                            val broadcast = mergeBroadcast(
                                    replyIndex,
                                    chatItemPojoByDate.replies,
                                    chatDateTime.blastId
                            )
                            val broadcastUiModel = createBroadCastUiModel(
                                    chatDateTime, broadcast.first
                            )
                            listChat.add(broadcastUiModel)
                            replyIndex += broadcast.second
                        }
                        // Merge product bubble
                        hasAttachment(chatDateTime) &&
                                chatDateTime.isAlsoProductAttachment(nextItem) -> {
                            val products = mergeProduct(
                                    replyIndex,
                                    chatItemPojoByDate.replies,
                                    chatDateTime.isBroadCast()
                            )
                            val carouselProducts = createCarouselProduct(chatDateTime, products)
                            listChat.add(carouselProducts)
                            replyIndex += products.size
                        }
                        // usual attachment
                        hasAttachment(chatDateTime) -> {
                            listChat.add(mapAttachment(chatDateTime))
                            replyIndex++
                        }
                        // text message
                        else -> {
                            val textMessage = convertToMessageViewModel(chatDateTime)
                            listChat.add(textMessage)
                            replyIndex++
                        }
                    }
                }
            }
        }
        return listChat
    }

    override fun convertToMessageViewModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return MessageViewModel(chatItemPojoByDateByTime)
    }

    private fun createBroadCastUiModel(chatDateTime: Reply, model: Map<String, Visitable<*>>): BroadCastUiModel {
        return BroadCastUiModel(chatDateTime, model, chatDateTime.isOpposite)
    }

    private fun mergeBroadcast(
            index: Int, replies: List<Reply>, blastId: Long
    ): Pair<Map<String, Visitable<*>>, Int> {
        val broadcast = ArrayMap<String, Visitable<*>>()
        var idx = index
        while (idx < replies.size) {
            val reply = replies[idx]
            val replyType = reply.attachmentType.toString()
            val nextReply = replies.getOrNull(idx + 1)
            if (
                    reply.isProductAttachment() &&
                    reply.isAlsoProductAttachment(nextReply) &&
                    reply.blastId == blastId
            ) {
                val products = mergeProduct(idx, replies, reply.isBroadCast())
                val carouselProducts = createCarouselProduct(reply, products)
                broadcast[TYPE_IMAGE_CAROUSEL] = carouselProducts
                idx += products.size
            } else if (reply.isBroadCast() && reply.blastId == blastId) {
                val messageItem = if (hasAttachment(reply)) {
                    mapAttachment(reply)
                } else {
                    convertToMessageViewModel(reply)
                }
                broadcast[replyType] = messageItem
                idx++
            } else {
                break
            }
        }
        val totalToSkip = idx - index
        return Pair(broadcast, totalToSkip)
    }

    private fun createHeaderDate(chatItemPojo: ChatRepliesItem): Visitable<*> {
        return HeaderDateUiModel(chatItemPojo.date)
    }

    private fun createCarouselProduct(chatDateTime: Reply, products: List<Visitable<*>>): ProductCarouselUiModel {
        with(chatDateTime) {
            return ProductCarouselUiModel(
                    products = products,
                    isSender = !chatDateTime.isOpposite,
                    messageId = msgId.toString(),
                    fromUid = senderId.toString(),
                    from = senderName,
                    fromRole = role,
                    attachmentId = attachment?.id ?: "",
                    attachmentType = attachment?.type.toString(),
                    replyTime = replyTime,
                    message = msg,
                    source = chatDateTime.source
            )
        }
    }

    private fun mergeProduct(index: Int, replies: List<Reply>, isBroadCast: Boolean): List<Visitable<*>> {
        val products = mutableListOf<Visitable<*>>()
        var idx = index
        while (idx < replies.size) {
            val chat = replies[idx]
            if (chat.isProductAttachment()) {
                val product = convertToProductAttachment(chat)
                products.add(product)
                idx++
            } else {
                break
            }
        }
        if (isBroadCast) {
            products.sortBy {
                return@sortBy (it as ProductAttachmentViewModel).hasEmptyStock()
            }
        }
        return products
    }

    override fun mapAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return when (chatItemPojoByDateByTime.attachment?.type.toString()) {
            TYPE_IMAGE_DUAL_ANNOUNCEMENT -> convertToDualAnnouncement(chatItemPojoByDateByTime)
            TYPE_VOUCHER -> convertToVoucher(chatItemPojoByDateByTime)
            TYPE_QUOTATION -> convertToQuotation(chatItemPojoByDateByTime)
            TYPE_STICKER.toString() -> convertToSticker(chatItemPojoByDateByTime)
            TYPE_REVIEW_REMINDER -> convertToReviewReminder(chatItemPojoByDateByTime)
            else -> super.mapAttachment(chatItemPojoByDateByTime)
        }
    }

    private fun convertToVoucher(item: Reply): Visitable<*> {
        val pojo = gson.fromJson<TopChatVoucherPojo>(item.attachment?.attributes, TopChatVoucherPojo::class.java)
        val voucher = pojo.voucher
        var voucherType = MerchantVoucherType(voucher.voucherType, "")
        var voucherAmount = MerchantVoucherAmount(voucher.amountType, voucher.amount)
        var voucherOwner = MerchantVoucherOwner(
                identifier = voucher.identifier,
                ownerId = voucher.ownerId.toIntOrZero()
        )
        var voucherBanner = MerchantVoucherBanner(mobileUrl = voucher.mobileUrl)
        var voucherModel = MerchantVoucherModel(
                voucherId = voucher.voucherId.toIntOrZero(),
                voucherName = voucher.voucherName,
                voucherCode = voucher.voucherCode,
                merchantVoucherType = voucherType,
                merchantVoucherAmount = voucherAmount,
                minimumSpend = voucher.minimumSpend.toIntOrZero(),
                merchantVoucherOwner = voucherOwner,
                validThru = voucher.validThru.toString(),
                tnc = voucher.tnc,
                merchantVoucherBanner = voucherBanner,
                merchantVoucherStatus = MerchantVoucherStatus()
        )

        return TopChatVoucherUiModel(
                item.msgId.toString(),
                item.senderId.toString(),
                item.senderName,
                item.role,
                item.attachment?.id ?: "",
                item.attachment?.type.toString(),
                item.replyTime,
                item.msg,
                item.isRead,
                false,
                !item.isOpposite,
                voucherModel,
                item.replyId.toString(),
                item.blastId.toString(),
                item.source,
                voucher.isPublic
        )
    }

    private fun convertToDualAnnouncement(item: Reply): Visitable<*> {
        val pojoAttribute = gson.fromJson<ImageDualAnnouncementPojo>(item.attachment?.attributes,
                ImageDualAnnouncementPojo::class.java)
        return ImageDualAnnouncementUiModel(
                messageId = item.msgId.toString(),
                fromUid = item.senderId.toString(),
                from = item.senderName,
                fromRole = item.role,
                attachmentId = item.attachment?.id ?: "",
                attachmentType = item.attachment?.type.toString(),
                replyTime = item.replyTime,
                message = item.msg,
                imageUrlTop = pojoAttribute.imageUrl,
                redirectUrlTop = pojoAttribute.url,
                imageUrlBottom = pojoAttribute.imageUrl2,
                redirectUrlBottom = pojoAttribute.url2,
                blastId = item.blastId,
                source = item.source
        )
    }

    private fun convertToQuotation(message: Reply): Visitable<*> {
        val quotationAttributes = gson
                .fromJson<QuotationAttributes>(
                        message.attachment?.attributes,
                        QuotationAttributes::class.java
                )
        return QuotationUiModel(
                quotationPojo = quotationAttributes.quotation,
                messageId = message.msgId.toString(),
                fromUid = message.senderId.toString(),
                from = message.senderName,
                fromRole = message.role,
                attachmentId = message.attachment?.id ?: "",
                attachmentType = message.attachment?.type.toString(),
                replyTime = message.replyTime,
                isSender = !message.isOpposite,
                message = message.msg,
                isRead = message.isRead,
                source = message.source
        )
    }

    private fun convertToSticker(message: Reply): Visitable<*> {
        val stickerAttributes = gson.fromJson<StickerAttributesResponse>(
                message.attachment?.attributes,
                StickerAttributesResponse::class.java
        )
        return StickerUiModel(
                messageId = message.msgId.toString(),
                fromUid = message.senderId.toString(),
                from = message.senderName,
                fromRole = message.role,
                attachmentId = message.attachment.id,
                attachmentType = message.attachment.type.toString(),
                replyTime = message.replyTime,
                message = message.msg,
                isRead = message.isRead,
                isDummy = false,
                isSender = !message.isOpposite,
                sticker = stickerAttributes.stickerProfile,
                source = message.source
        )
    }

    private fun convertToReviewReminder(message: Reply): Visitable<*> {
        val review = gson.fromJson<ReviewReminderAttribute>(
                message.attachment.attributes,
                ReviewReminderAttribute::class.java
        )
        return ReviewUiModel(message, review.reviewCard)
    }
}

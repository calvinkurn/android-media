package com.tokopedia.topchat.chatroom.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_DUAL_ANNOUNCEMENT
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUOTATION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_STICKER
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.ChatRepliesItem
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.domain.pojo.ImageDualAnnouncementPojo
import com.tokopedia.topchat.chatroom.domain.pojo.QuotationAttributes
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatVoucherPojo
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerAttributesResponse
import com.tokopedia.topchat.chatroom.view.uimodel.HeaderDateUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel
import javax.inject.Inject

/**
 * @author : Steven 02/01/19
 */

open class TopChatRoomGetExistingChatMapper @Inject constructor(
        private val useNewCard: Boolean,
        private val useCarousel: Boolean
) : GetExistingChatMapper() {

    override fun mappingListChat(pojo: GetExistingChatPojo): ArrayList<Visitable<*>> {
        val listChat: ArrayList<Visitable<*>> = ArrayList()
        val replies = pojo.chatReplies.list
        for ((index, chatItemPojo) in replies.withIndex()) {
            listChat.add(createHeaderDate(chatItemPojo))
            if (index == replies.lastIndex) {
                latestHeaderDate = chatItemPojo.date
            }
            for (chatItemPojoByDate in chatItemPojo.chats) {
                var index = 0
                while (index < chatItemPojoByDate.replies.size) {
                    val chatDateTime = chatItemPojoByDate.replies[index]
                    if (hasAttachment(chatDateTime)) {
                        val nextItem = chatItemPojoByDate.replies.getOrNull(index + 1)
                        if (useNewCard && useCarousel && chatDateTime.isMultipleProductAttachment(nextItem)) {
                            val products = mergeProduct(index, chatItemPojoByDate.replies)
                            val carouselProducts = createCarouselProduct(chatDateTime, products)
                            listChat.add(carouselProducts)
                            index += products.size
                        } else {
                            listChat.add(mapAttachment(chatDateTime))
                            index++
                        }
                    } else {
                        listChat.add(convertToMessageViewModel(chatDateTime))
                        index++
                    }
                }
            }
        }
        return listChat
    }

    private fun createHeaderDate(chatItemPojo: ChatRepliesItem): Visitable<*> {
        return HeaderDateUiModel(chatItemPojo.date)
    }

    private fun createCarouselProduct(chatDateTime: Reply, products: List<Visitable<*>>): ProductCarouselUiModel {
        with(chatDateTime) {
            return ProductCarouselUiModel(
                    products = products,
                    messageId = msgId.toString(),
                    fromUid = senderId.toString(),
                    from = senderName,
                    fromRole = role,
                    attachmentId = attachment?.id ?: "",
                    attachmentType = attachment?.type.toString(),
                    replyTime = replyTime,
                    message = msg
            )
        }
    }

    private fun mergeProduct(index: Int, replies: List<Reply>): List<Visitable<*>> {
        val products = mutableListOf<Visitable<*>>()
        var idx = index
        while (idx < replies.size) {
            val chat = replies[idx]
            if (chat.isProductAttachment()) {
                products.add(convertToProductAttachment(chat))
                idx++
            } else {
                break
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
            else -> super.mapAttachment(chatItemPojoByDateByTime)
        }
    }

    private fun convertToVoucher(item: Reply): Visitable<*> {
        var temp = item.attachment?.attributes

        val pojo = GsonBuilder().create().fromJson<TopChatVoucherPojo>(MethodChecker.fromHtml(temp).toString(),
                TopChatVoucherPojo::class.java)
        val voucher = pojo.voucher
        var voucherType = MerchantVoucherType(voucher.voucherType, "")
        var voucherAmount = MerchantVoucherAmount(voucher.amountType, voucher.amount)
        var voucherOwner = MerchantVoucherOwner(identifier = voucher.identifier, ownerId = voucher.ownerId)
        var voucherBanner = MerchantVoucherBanner(mobileUrl = voucher.mobileUrl)
        var voucherModel = MerchantVoucherModel(voucherId = voucher.voucherId,
                voucherName = voucher.voucherName,
                voucherCode = voucher.voucherCode,
                merchantVoucherType = voucherType,
                merchantVoucherAmount = voucherAmount,
                minimumSpend = voucher.minimumSpend,
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
                item.blastId.toString()
        )
    }

    private fun convertToDualAnnouncement(item: Reply): Visitable<*> {
        val pojoAttribute = GsonBuilder().create().fromJson<ImageDualAnnouncementPojo>(item.attachment?.attributes,
                ImageDualAnnouncementPojo::class.java)
        return ImageDualAnnouncementUiModel(
                item.msgId.toString(),
                item.senderId.toString(),
                item.senderName,
                item.role,
                item.attachment?.id ?: "",
                item.attachment?.type.toString(),
                item.replyTime,
                item.msg,
                pojoAttribute.imageUrl,
                pojoAttribute.url,
                pojoAttribute.imageUrl2,
                pojoAttribute.url2,
                item.blastId
        )
    }

    private fun convertToQuotation(message: Reply): Visitable<*> {
        val quotationAttributes = GsonBuilder()
                .create()
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
                isRead = message.isRead
        )
    }

    private fun convertToSticker(message: Reply): Visitable<*> {
        val stickerAttributes = GsonBuilder()
                .create()
                .fromJson<StickerAttributesResponse>(
                        message.attachment?.attributes,
                        StickerAttributesResponse::class.java
                )
        return StickerUiModel(
                messageId = message.msgId.toString(),
                fromUid = message.senderId.toString(),
                from = message.senderName,
                fromRole = message.role,
                attachmentId = message.attachment?.id ?: "",
                attachmentType = message.attachment?.type.toString(),
                replyTime = message.replyTime,
                message = message.msg,
                isRead = message.isRead,
                isDummy = false,
                isSender = !message.isOpposite,
                sticker = stickerAttributes.stickerProfile
        )
    }
}

package com.tokopedia.topchat.chatroom.domain.mapper

import androidx.collection.ArrayMap
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CTA_HEADER_MSG
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_CAROUSEL
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_DUAL_ANNOUNCEMENT
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_ORDER_CANCELLATION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_BUNDLING
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_REVIEW_REMINDER
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_STICKER
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.data.AutoReplyMessageUiModel
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.ChatRepliesItem
import com.tokopedia.chat_common.domain.pojo.Contact
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.imageannouncement.ImageAnnouncementPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.domain.pojo.roommetadata.User
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.merchantvoucher.common.gql.data.*
import com.tokopedia.topchat.chatroom.domain.pojo.ImageDualAnnouncementPojo
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatVoucherPojo
import com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg.HeaderCtaButtonAttachment
import com.tokopedia.topchat.chatroom.domain.pojo.ordercancellation.TopChatRoomOrderCancellationPojo
import com.tokopedia.topchat.chatroom.domain.pojo.product_bundling.ProductBundlingPojo
import com.tokopedia.topchat.chatroom.domain.pojo.review.ReviewReminderAttribute
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerAttributesResponse
import com.tokopedia.topchat.chatroom.view.uimodel.*
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel
import javax.inject.Inject

/**
 * @author : Steven 02/01/19
 */

open class TopChatRoomGetExistingChatMapper @Inject constructor() : GetExistingChatMapper() {

    override fun mappingListChat(pojo: GetExistingChatPojo): ArrayList<Visitable<*>> {
        val listChat: ArrayList<Visitable<*>> = ArrayList()
        val replies = pojo.chatReplies.list
        val attachmentIds = getAttachmentIds(pojo.chatReplies.attachmentIds)
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
                        chatDateTime.status == BaseChatUiModel.STATUS_DELETED -> {
                            val textMessage = convertToMessageUiModel(chatDateTime)
                            listChat.add(textMessage)
                            replyIndex++
                        }
                        // Merge broadcast bubble
                        chatDateTime.isBroadCast() &&
                                chatDateTime.isAlsoTheSameBroadcast(nextItem) -> {
                            val broadcast = mergeBroadcast(
                                replyIndex,
                                chatItemPojoByDate.replies,
                                chatDateTime.blastId,
                                getAttachmentIds(pojo.chatReplies.attachmentIds)
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
                                chatDateTime.isBroadCast(),
                                attachmentIds
                            )
                            val carouselProducts = createCarouselProduct(chatDateTime, products)
                            listChat.add(carouselProducts)
                            replyIndex += products.size
                        }
                        // usual attachment
                        hasAttachment(chatDateTime) -> {
                            listChat.add(mapAttachment(chatDateTime, attachmentIds))
                            replyIndex++
                        }
                        // text message
                        else -> {
                            val textMessage = convertToMessageUiModel(chatDateTime)
                            listChat.add(textMessage)
                            replyIndex++
                        }
                    }
                }
            }
        }
        return listChat
    }

    override fun convertToMessageUiModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return if (chatItemPojoByDateByTime.isAutoReply()) {
            convertToAutoReplyUiModel(chatItemPojoByDateByTime)
        } else {
            convertToRegularMessageUiModel(chatItemPojoByDateByTime)
        }
    }

    private fun convertToRegularMessageUiModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val msg = MessageUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
        if (chatItemPojoByDateByTime.status == BaseChatUiModel.STATUS_DELETED) {
            msg.withMarkAsDeleted()
        }
        return msg.build()
    }

    /**
     * Auto reply message has the same data structure from GQL but different view
     * As the message itself is Array of JSON String, not regular string
     * To save some resources (RV in each bubbles message is a waste), the separation is inevitable
     */
    private fun convertToAutoReplyUiModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val autoReplyMessage = AutoReplyMessageUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
        if (chatItemPojoByDateByTime.status == BaseChatUiModel.STATUS_DELETED) {
            autoReplyMessage.withMarkAsDeleted()
        }
        return autoReplyMessage.build()
    }

    private fun createBroadCastUiModel(
        chatDateTime: Reply,
        model: Map<String, Visitable<*>>
    ): BroadCastUiModel {
        return BroadCastUiModel(chatDateTime, model, chatDateTime.isOpposite)
    }

    private fun mergeBroadcast(
        index: Int, replies: List<Reply>, blastId: String, attachmentIds: List<String>
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
                val products = mergeProduct(idx, replies, reply.isBroadCast(), attachmentIds)
                val carouselProducts = createCarouselProduct(reply, products)
                broadcast[TYPE_IMAGE_CAROUSEL] = carouselProducts
                idx += products.size
            } else if (reply.isBroadCast() && reply.blastId == blastId) {
                val messageItem = if (hasAttachment(reply)) {
                    mapAttachment(reply, attachmentIds)
                } else {
                    convertToMessageUiModel(reply)
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

    private fun createCarouselProduct(
        chatDateTime: Reply,
        products: List<Visitable<*>>
    ): ProductCarouselUiModel {
        with(chatDateTime) {
            return ProductCarouselUiModel(
                products = products,
                isSender = !chatDateTime.isOpposite,
                messageId = msgId.toString(),
                fromUid = senderId.toString(),
                from = senderName,
                fromRole = role,
                attachmentId = attachment.id,
                attachmentType = attachment.type.toString(),
                replyTime = replyTime,
                message = msg,
                source = chatDateTime.source
            )
        }
    }

    private fun mergeProduct(
        index: Int,
        replies: List<Reply>,
        isBroadCast: Boolean,
        attachmentIds: List<String>
    ): List<Visitable<*>> {
        val products = mutableListOf<Visitable<*>>()
        var idx = index
        while (idx < replies.size) {
            val chat = replies[idx]
            if (chat.isProductAttachment()) {
                val product = convertToProductAttachment(chat, attachmentIds)
                products.add(product)
                idx++
            } else {
                break
            }
        }
        if (isBroadCast) {
            products.sortBy {
                return@sortBy (it as ProductAttachmentUiModel).hasEmptyStock()
            }
        }
        return products
    }

    override fun mapAttachment(
        chatItemPojoByDateByTime: Reply,
        attachmentIds: List<String>
    ): Visitable<*> {
        return when (chatItemPojoByDateByTime.attachment.type.toString()) {
            TYPE_IMAGE_DUAL_ANNOUNCEMENT -> convertToDualAnnouncement(chatItemPojoByDateByTime)
            TYPE_VOUCHER -> convertToVoucher(chatItemPojoByDateByTime)
            TYPE_STICKER.toString() -> convertToSticker(chatItemPojoByDateByTime)
            TYPE_REVIEW_REMINDER -> convertToReviewReminder(chatItemPojoByDateByTime)
            TYPE_CTA_HEADER_MSG -> convertToCtaHeaderMsg(chatItemPojoByDateByTime)
            TYPE_PRODUCT_BUNDLING -> convertToProductBundling(
                chatItemPojoByDateByTime,
                attachmentIds
            )
            TYPE_ORDER_CANCELLATION -> convertToOrderCancellation(
                chatItemPojoByDateByTime
            )
            else -> super.mapAttachment(chatItemPojoByDateByTime, attachmentIds)
        }
    }

    private fun convertToCtaHeaderMsg(reply: Reply): Visitable<*> {
        val attachment = gson.fromJson(
            reply.attachment.attributes, HeaderCtaButtonAttachment::class.java
        )
        return MessageUiModel.Builder()
            .withResponseFromGQL(reply)
            .withAttachment(attachment)
            .build()
    }

    override fun convertToImageAnnouncement(item: Reply): Visitable<*> {
        val pojoAttribute = gson.fromJson(
            item.attachment.attributes, ImageAnnouncementPojo::class.java
        )
        return ImageAnnouncementUiModel(item, pojoAttribute)
    }

    private fun convertToVoucher(item: Reply): Visitable<*> {
        val pojo = gson.fromJson<TopChatVoucherPojo>(
            item.attachment.attributes,
            TopChatVoucherPojo::class.java
        )
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

        return TopChatVoucherUiModel.Builder()
            .withResponseFromGQL(item)
            .withVoucherModel(voucherModel)
            .withIsPublic(voucher.isPublic)
            .withIsLockToProduct(voucher.isLockToProduct ?: 0)
            .withApplink(voucher.applink ?: "")
            .build()
    }

    private fun convertToDualAnnouncement(item: Reply): Visitable<*> {
        val pojoAttribute = gson.fromJson(
            item.attachment.attributes,
            ImageDualAnnouncementPojo::class.java
        )
        return ImageDualAnnouncementUiModel(
            messageId = item.msgId,
            fromUid = item.senderId,
            from = item.senderName,
            fromRole = item.role,
            attachmentId = item.attachment.id,
            attachmentType = item.attachment.type.toString(),
            replyTime = item.replyTime,
            message = item.msg,
            imageUrlTop = pojoAttribute.imageUrl,
            redirectUrlTop = pojoAttribute.url,
            imageUrlBottom = pojoAttribute.imageUrl2,
            redirectUrlBottom = pojoAttribute.url2,
            broadcastBlastId = item.blastId,
            source = item.source
        )
    }

    private fun convertToSticker(message: Reply): Visitable<*> {
        val stickerAttributes = gson.fromJson(
            message.attachment.attributes,
            StickerAttributesResponse::class.java
        )
        return StickerUiModel.Builder()
            .withResponseFromGQL(message)
            .withStickerProfile(stickerAttributes.stickerProfile)
            .build()
    }

    private fun convertToReviewReminder(message: Reply): Visitable<*> {
        val review = gson.fromJson(
            message.attachment.attributes,
            ReviewReminderAttribute::class.java
        )
        return ReviewUiModel.Builder()
            .withResponseFromGQL(message)
            .withReply(message)
            .withReviewCard(review.reviewCard)
            .build()
    }

    fun generateRoomMetaData(
        messageId: String,
        chat: GetExistingChatPojo
    ): RoomMetaData {
        val interlocutor = chat.chatReplies.getInterlocutorContact()
        val sender = chat.chatReplies.getSenderContact()
        val interlocutorMetaData = User(
            name = interlocutor.name,
            uid = interlocutor.userId.toString(),
            uname = interlocutor.name,
            role = interlocutor.role,
            thumbnail = interlocutor.thumbnail
        )
        val senderMetaData = User(
            name = sender.name,
            uid = sender.userId.toString(),
            uname = sender.name,
            role = sender.role,
            thumbnail = sender.thumbnail
        )
        val userIdMap = mapUserId(chat.chatReplies.contacts)
        return RoomMetaData(
            _msgId = messageId,
            sender = senderMetaData,
            receiver = interlocutorMetaData,
            userIdMap = userIdMap
        )
    }

    private fun mapUserId(contacts: List<Contact>): Map<String, User> {
        return contacts.associateBy(
            { it.userId.toString() },
            {
                User(
                    name = it.name,
                    uid = it.userId.toString(),
                    uname = it.name,
                    role = it.role,
                    thumbnail = it.thumbnail
                )
            }
        )
    }

    private fun convertToProductBundling(item: Reply, attachmentIds: List<String>): Visitable<*> {
        val pojo = gson.fromJson(
            item.attachment.attributes,
            ProductBundlingPojo::class.java
        )
        val needSync = attachmentIds.contains(item.attachment.id)
        return if (pojo.listProductBundling.size == 1) {
            ProductBundlingUiModel.Builder()
                .withResponseFromGQL(item)
                .withIsSender(!item.isOpposite)
                .withNeedSync(needSync)
                .withProductBundling(pojo.listProductBundling.first())
                .build()
        } else {
            MultipleProductBundlingUiModel.Builder()
                .withResponseFromGQL(item)
                .withIsSender(!item.isOpposite)
                .withNeedSync(needSync)
                .withProductBundlingResponse(pojo.listProductBundling)
                .build()
        }
    }

    private fun convertToOrderCancellation(item: Reply): Visitable<*> {
        val pojo = gson.fromJson(
            item.attachment.attributes,
            TopChatRoomOrderCancellationPojo::class.java
        )
        return TopChatRoomOrderCancellationUiModel.Builder()
            .withResponseFromGQL(item)
            .withOrderId(pojo.orderId)
            .withTitle(pojo.button.title)
            .withAppLink(pojo.button.appLink)
            .build()
    }
}

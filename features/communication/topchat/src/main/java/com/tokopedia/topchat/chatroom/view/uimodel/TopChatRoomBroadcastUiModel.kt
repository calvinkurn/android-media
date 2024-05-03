package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.*
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_ANNOUNCEMENT
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_VOUCHER_CAROUSEL_ATTACHMENT
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomTypeFactory
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel

class TopChatRoomBroadcastUiModel(
    val reply: Reply,
    private val items: Map<String, Visitable<*>>,
    val isOpposite: Boolean
) : BaseChatUiModel(
    messageId = reply.msgId,
    fromUid = reply.senderId,
    from = reply.senderName,
    fromRole = reply.role,
    attachmentId = reply.attachment.id,
    attachmentType = reply.attachment.type.toString(),
    replyTime = reply.replyTime,
    message = reply.msg,
    source = reply.source,
    blastId = reply.blastId,
    messageType = reply.messageType
),
    Visitable<TopChatRoomTypeFactory> {

    val banner: ImageAnnouncementUiModel? get() = items[TYPE_IMAGE_ANNOUNCEMENT] as? ImageAnnouncementUiModel
    val singleVoucher: TopChatRoomVoucherUiModel? get() = items[TYPE_VOUCHER] as? TopChatRoomVoucherUiModel
    val voucherCarousel: TopChatRoomVoucherCarouselUiModel? get() = items[TYPE_VOUCHER_CAROUSEL_ATTACHMENT] as? TopChatRoomVoucherCarouselUiModel
    val productCarousel: TopChatRoomProductCarouselUiModel? get() = items[AttachmentType.Companion.TYPE_PRODUCT_CAROUSEL_ATTACHMENT] as? TopChatRoomProductCarouselUiModel
    val singleProduct: ProductAttachmentUiModel? get() = items[AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT] as? ProductAttachmentUiModel
    val productBundling: Visitable<*>? get() = items[AttachmentType.Companion.TYPE_PRODUCT_BUNDLING]
    val messageUiModel: MessageUiModel? get() = items[AttachmentType.Companion.TYPE_MESSAGE] as? MessageUiModel

    override fun type(typeFactory: TopChatRoomTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasVoucher(): Boolean {
        return singleVoucher != null
    }

    fun isSingleProduct(): Boolean {
        return singleProduct != null
    }

    fun hasCampaignLabel(): Boolean {
        return banner?.isCampaign == true
    }
}

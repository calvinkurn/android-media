package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

class BroadCastUiModel : BaseChatViewModel, Visitable<TopChatTypeFactory> {

    val items: Map<String, Visitable<*>>
    val banner: ImageAnnouncementViewModel? get() = items[AttachmentType.Companion.TYPE_IMAGE_ANNOUNCEMENT] as? ImageAnnouncementViewModel
    val voucherUiModel: TopChatVoucherUiModel? get() = items[AttachmentType.Companion.TYPE_VOUCHER] as? TopChatVoucherUiModel
    val productCarousel: ProductCarouselUiModel? get() = items[AttachmentType.Companion.TYPE_IMAGE_CAROUSEL] as? ProductCarouselUiModel

    constructor(reply: Reply, items: Map<String, Visitable<*>>) : super(
            messageId = reply.msgId.toString(),
            fromUid = reply.senderId.toString(),
            from = reply.senderName,
            fromRole = reply.role,
            attachmentId = reply.attachment?.id ?: "",
            attachmentType = reply.attachment?.type.toString(),
            replyTime = reply.replyTime,
            message = reply.msg,
            source = reply.source
    ) {
        this.items = items
    }

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
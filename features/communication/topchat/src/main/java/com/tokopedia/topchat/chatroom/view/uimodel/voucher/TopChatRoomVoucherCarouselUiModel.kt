package com.tokopedia.topchat.chatroom.view.uimodel.voucher

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomTypeFactory

class TopChatRoomVoucherCarouselUiModel(
    val vouchers: List<TopChatRoomVoucherUiModel>,
    val isSender: Boolean,
    messageId: String,
    fromUid: String?,
    from: String,
    fromRole: String,
    attachmentId: String,
    attachmentType: String,
    replyTime: String?,
    message: String,
    source: String
): BaseChatUiModel(
    messageId, fromUid, from, fromRole,
    attachmentId, attachmentType, replyTime, message, source
),
    Visitable<TopChatRoomTypeFactory> {
    override fun type(typeFactory: TopChatRoomTypeFactory): Int {
        return typeFactory.type(this)
    }
}

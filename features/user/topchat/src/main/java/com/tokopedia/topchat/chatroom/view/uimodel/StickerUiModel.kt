package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class StickerUiModel(
        messageId: String,
        fromUid: String,
        from: String,
        fromRole: String,
        attachmentId: String,
        attachmentType: String,
        replyTime: String,
        message: String,
        isRead: Boolean,
        isDummy: Boolean,
        isSender: Boolean,
        val sticker: StickerProfile
) : SendableViewModel(messageId,
        fromUid,
        from,
        fromRole,
        attachmentId,
        attachmentType,
        replyTime,
        "",
        isRead,
        isDummy,
        isSender,
        message), Visitable<TopChatTypeFactory> {
    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
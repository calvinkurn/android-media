package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class StickerUiModel : SendableViewModel, Visitable<TopChatTypeFactory> {

    val sticker: StickerProfile

    /**
     * Constructor when message received from WebSocket and GQL
     */
    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, startTime: String = "",
            message: String, isRead: Boolean, isDummy: Boolean, isSender: Boolean,
            sticker: StickerProfile, source: String
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, startTime,
            isRead, isDummy, isSender, message, source
    ) {
        this.sticker = sticker
    }

    /**
     * Constructor for sending dummy
     */
    constructor(
            messageId: String, fromUid: String, from: String, startTime: String,
            sticker: StickerProfile
    ) : super(
            messageId, fromUid, from, "",
            "", "", BaseChatViewModel.SENDING_TEXT, startTime,
            false, true, true, sticker.intention,
            ""
    ) {
        this.sticker = sticker
    }


    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
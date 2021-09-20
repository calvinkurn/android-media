package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.user.session.UserSessionInterface

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

    companion object {
        fun generatePreviewMessage(
            roomMetaData: RoomMetaData,
            userSession: UserSessionInterface,
            sticker: Sticker
        ): StickerUiModel {
            val startTime = generateStartTime()
            return StickerUiModel(
                roomMetaData.msgId,
                userSession.userId,
                userSession.name,
                startTime,
                sticker.generateStickerProfile()
            )
        }
    }
}
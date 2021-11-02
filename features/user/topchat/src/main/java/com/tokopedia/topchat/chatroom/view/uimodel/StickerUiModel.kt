package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.attachment.AttachmentId
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class StickerUiModel private constructor(
    builder: Builder
) : SendableUiModel(builder), Visitable<TopChatTypeFactory> {

    val sticker: StickerProfile = builder.stickerProfile

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun generatePreviewMessage(
            roomMetaData: RoomMetaData,
            sticker: Sticker,
            referredMsg: ParentReply?
        ): StickerUiModel {
            val stickerProfile = StickerProfile(
                groupId = sticker.groupUUID,
                imageUrl = sticker.imageUrl,
                intention = sticker.intention,
                stickerId = sticker.stickerUUID
            )
            return Builder()
                .withRoomMetaData(roomMetaData)
                .withAttachmentId(AttachmentId.NOT_YET_GENERATED)
                .withAttachmentType(AttachmentType.Companion.TYPE_STICKER.toString())
                .withStickerProfile(stickerProfile)
                .withParentReply(referredMsg)
                .build()
        }
    }

    class Builder : SendableUiModel.Builder<Builder, StickerUiModel>() {

        internal var stickerProfile: StickerProfile = StickerProfile()

        fun withStickerProfile(stickerProfile: StickerProfile): Builder {
            this.stickerProfile = stickerProfile
            return self()
        }

        override fun build(): StickerUiModel {
            return StickerUiModel(this)
        }
    }
}
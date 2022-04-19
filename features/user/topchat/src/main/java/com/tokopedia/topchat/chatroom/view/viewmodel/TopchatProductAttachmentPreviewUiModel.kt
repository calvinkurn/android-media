package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderUiModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory

class TopchatProductAttachmentPreviewUiModel(
    builder: Builder
) : ProductAttachmentUiModel(builder), SendablePreview {

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun generateMsgObj(
        roomMetaData: RoomMetaData,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): Any {
        val startTime = generateStartTime()
        val msgId = roomMetaData.msgId
        val toUid = roomMetaData.receiver.uid
        return TopChatWebSocketParam.generateParamSendProductAttachment(
            this, msgId, startTime,
            toUid, message, userLocationInfo,
            localId
        )
    }

    override fun notEnoughRequiredData(): Boolean {
        return isLoading
    }

    override fun generatePreviewMessage(
        roomMetaData: RoomMetaData,
        message: String
    ): SendableUiModel {
        return this.apply {
            updateCanShowFooter(canShowFooterProductAttachment(
                true, roomMetaData.sender.role
            ))
        }
    }

    private fun canShowFooterProductAttachment(isOpposite: Boolean, role: String): Boolean {
        return (!isOpposite && role.equals(ChatRoomHeaderUiModel.Companion.ROLE_USER, ignoreCase = true))
                || (isOpposite && !role.equals(ChatRoomHeaderUiModel.Companion.ROLE_USER, ignoreCase = true))
    }

    class Builder : ProductAttachmentUiModel.Builder() {
        override fun build(): TopchatProductAttachmentPreviewUiModel {
            return TopchatProductAttachmentPreviewUiModel(this)
        }
    }
}
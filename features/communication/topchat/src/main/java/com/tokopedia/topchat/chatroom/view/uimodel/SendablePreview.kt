package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory

interface SendablePreview {
    fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int
    fun generateMsgObj(
        roomMetaData: RoomMetaData,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String,
        sourceReply: String
    ): Any
    fun notEnoughRequiredData(): Boolean
    fun generatePreviewMessage(
        roomMetaData: RoomMetaData,
        message: String
    ): SendableUiModel
}

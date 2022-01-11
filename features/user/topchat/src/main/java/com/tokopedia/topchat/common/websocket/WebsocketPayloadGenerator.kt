package com.tokopedia.topchat.common.websocket

import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview

interface WebsocketPayloadGenerator {
    fun generatePreviewMsg(
        message: String,
        intention: String? = null,
        roomMetaData: RoomMetaData,
        referredMsg: ParentReply? = null
    ): SendableUiModel
    fun generateWsPayload(
        message: String,
        intention: String? = null,
        roomMetaData: RoomMetaData,
        previewMsg: SendableUiModel,
        attachments: List<SendablePreview>,
        userLocationInfo: LocalCacheModel? = null,
        referredMsg: ParentReply? = null
    ): String
    fun generatePreviewMessage(
        sendablePreview: SendablePreview,
        roomMetaData: RoomMetaData,
        message: String
    ): SendableUiModel
    fun generateWsPayload(
        sendablePreview: SendablePreview,
        roomMetaData: RoomMetaData,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): String

    fun generateWsPayloadStopTyping(msgId: String): String
    fun generateMarkAsReadPayload(roomMetaData: RoomMetaData): String
}
package com.tokopedia.topchat.common.websocket

import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import java.util.ArrayList

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

    fun generateAttachmentPreviewMsg(
        sendablePreview: SendablePreview,
        roomMetaData: RoomMetaData,
        message: String
    ): SendableUiModel

    fun generateAttachmentWsPayload(
        sendablePreview: SendablePreview,
        roomMetaData: RoomMetaData,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): String

    fun generateWsPayloadStopTyping(msgId: String): String
    fun generateWsPayloadStartTyping(msgId: String): String
    fun generateMarkAsReadPayload(roomMetaData: RoomMetaData): String
    fun generateStickerPreview(
        roomMetaData: RoomMetaData,
        sticker: Sticker,
        referredMsg: ParentReply?
    ): SendableUiModel

    fun generateStickerWsPayload(
        sticker: Sticker,
        roomMetaData: RoomMetaData,
        attachments: ArrayList<SendablePreview>,
        localId: String,
        referredMsg: ParentReply?
    ): String

    fun generateImageWsPayload(
        roomMetaData: RoomMetaData,
        filePath: String,
        imageUploadUiModel: ImageUploadUiModel,
        isSecure: Boolean
    ): String

    fun generateLocalId(): String
}

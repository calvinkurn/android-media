package com.tokopedia.topchat.common.websocket

import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.Sticker
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.chatroom.view.uimodel.StickerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.user.session.UserSessionInterface
import java.util.ArrayList
import javax.inject.Inject

open class DefaultWebsocketPayloadGenerator @Inject constructor(
    private val userSession: UserSessionInterface
) : WebsocketPayloadGenerator {

    override fun generateLocalId(): String {
        return IdentifierUtil.generateLocalId()
    }

    override fun generatePreviewMsg(
        message: String,
        intention: String?,
        roomMetaData: RoomMetaData,
        referredMsg: ParentReply?
    ): SendableUiModel {
        val startTime = SendableUiModel.generateStartTime()
        val localId = generateLocalId()
        return MessageUiModel.Builder()
            .withMsgId(roomMetaData.msgId)
            .withFromUid(userSession.userId)
            .withFrom(userSession.name)
            .withReplyTime(BaseChatUiModel.Builder.generateCurrentReplyTime())
            .withStartTime(startTime)
            .withMsg(message)
            .withLocalId(localId)
            .withParentReply(referredMsg)
            .withIsDummy(true)
            .withIsSender(true)
            .withIsRead(false)
            .build()
    }

    override fun generateWsPayload(
        message: String,
        intention: String?,
        roomMetaData: RoomMetaData,
        previewMsg: SendableUiModel,
        attachments: List<SendablePreview>,
        userLocationInfo: LocalCacheModel?,
        referredMsg: ParentReply?
    ): String {
        val startTime = SendableUiModel.generateStartTime()
        return TopChatWebSocketParam.generateParamSendMessage(
            roomeMetaData = roomMetaData,
            messageText = message,
            startTime = startTime,
            attachments = attachments,
            localId = previewMsg.localId,
            intention = intention,
            userLocationInfo = userLocationInfo,
            referredMsg = referredMsg
        )
    }

    override fun generateAttachmentWsPayload(
        sendablePreview: SendablePreview,
        roomMetaData: RoomMetaData,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): String {
        return sendablePreview.generateMsgObj(
            roomMetaData, message, userLocationInfo, localId
        ).toString()
    }

    override fun generateAttachmentPreviewMsg(
        sendablePreview: SendablePreview,
        roomMetaData: RoomMetaData,
        message: String
    ): SendableUiModel {
        return sendablePreview.generatePreviewMessage(
            roomMetaData, message
        )
    }

    override fun generateWsPayloadStopTyping(msgId: String): String {
        return TopChatWebSocketParam.generateParamStopTyping(msgId)
    }

    override fun generateWsPayloadStartTyping(msgId: String): String {
        return TopChatWebSocketParam.generateParamStartTyping(msgId)
    }

    override fun generateMarkAsReadPayload(roomMetaData: RoomMetaData): String {
        return TopChatWebSocketParam.generateParamRead(roomMetaData.msgId)
    }

    override fun generateStickerPreview(
        roomMetaData: RoomMetaData,
        sticker: Sticker,
        referredMsg: ParentReply?
    ): SendableUiModel {
        return StickerUiModel.generatePreviewMessage(roomMetaData, sticker, referredMsg)
    }

    override fun generateStickerWsPayload(
        sticker: Sticker,
        roomMetaData: RoomMetaData,
        attachments: ArrayList<SendablePreview>,
        localId: String,
        referredMsg: ParentReply?
    ): String {
        val startTime = SendableUiModel.generateStartTime()
        val contract = sticker.generateWebSocketPayload(
            messageId = roomMetaData.msgId,
            startTime = startTime,
            attachments = attachments,
            localId = localId,
            referredMsg = referredMsg
        )
        return CommonUtil.toJson(contract)
    }

    override fun generateImageWsPayload(
        roomMetaData: RoomMetaData,
        uploadId: String,
        imageUploadUiModel: ImageUploadUiModel
    ): String {
        return TopChatWebSocketParam.generateParamSendImage(
            roomMetaData.msgId, uploadId, imageUploadUiModel
        )
    }
}
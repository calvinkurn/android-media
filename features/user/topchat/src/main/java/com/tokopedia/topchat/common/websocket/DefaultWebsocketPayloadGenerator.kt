package com.tokopedia.topchat.common.websocket

import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class DefaultWebsocketPayloadGenerator @Inject constructor(
    private val userSession: UserSessionInterface
): WebsocketPayloadGenerator {

    override fun generatePreviewMsg(
        message: String,
        intention: String?,
        roomMetaData: RoomMetaData,
        referredMsg: ParentReply?
    ): SendableUiModel {
        val startTime = SendableUiModel.generateStartTime()
        val localId = IdentifierUtil.generateLocalId()
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

    override fun generateWsPayloadStopTyping(msgId: String): String {
        return TopChatWebSocketParam.generateParamStopTyping(msgId)
    }
}
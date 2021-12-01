package com.tokopedia.topchat.chatroom.domain.pojo.sticker


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_STICKER
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.WebsocketAttachmentContract
import com.tokopedia.topchat.chatroom.view.viewmodel.WebsocketAttachmentData

data class Sticker(
        @SerializedName("groupUUID")
        val groupUUID: String = "",
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("intention")
        val intention: String = "",
        @SerializedName("stickerUUID")
        val stickerUUID: String = ""
) {

    fun generateWebSocketPayload(
            messageId: String,
            startTime: String,
            attachments: List<SendablePreview>,
            localId: String,
            referredMsg: ParentReply?
    ): WebsocketAttachmentContract {
        val referredMsgRequest = TopChatWebSocketParam
            .generateParentReplyRequestPayload(referredMsg)
        val payload = WebSocketStickerPayload(
                groupUUID, stickerUUID, imageUrl, intention
        )
        val data = WebsocketAttachmentData(
                message_id = messageId.toLongOrZero(),
                local_id = localId,
                message = intention,
                source = "inbox",
                attachment_type = TYPE_STICKER,
                start_time = startTime,
                payload = payload,
                parent_reply = referredMsgRequest
        )
        if (attachments.isNotEmpty()) {
            data.addExtrasAttachments(attachments)
        }
        return WebsocketAttachmentContract(
                WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE,
                data
        )
    }

    fun generateStickerProfile(): StickerProfile {
        return StickerProfile(groupUUID, imageUrl, intention, stickerUUID)
    }

    @Keep
    class WebSocketStickerPayload(
            val group_id: String,
            val sticker_id: String,
            val image_url: String,
            val intention: String
    )
}
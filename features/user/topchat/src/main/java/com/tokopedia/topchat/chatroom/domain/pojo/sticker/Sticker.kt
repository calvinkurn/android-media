package com.tokopedia.topchat.chatroom.domain.pojo.sticker


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_STICKER
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr.StickerProfile
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
            opponentId: String,
            startTime: String
    ): WebsocketAttachmentContract {
        val payload = WebSocketStickerPayload(
                groupUUID, stickerUUID, imageUrl, intention
        )
        val data = WebsocketAttachmentData(
                messageId.toInt(),
                intention,
                "inbox",
                TYPE_STICKER,
                startTime,
                payload
        )
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
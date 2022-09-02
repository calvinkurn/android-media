package com.tokopedia.topchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 14/01/19.
 */
class ChangeChatBlockPojo(
        @SerializedName("chatToggleBlockChat")
        @Expose
        val chatToggleBlockChat: ChatToggleBlockChat = ChatToggleBlockChat()
) {

    data class ChatToggleBlockChat(

            @SerializedName("success")
            @Expose
            var success: Boolean = false,
            @SerializedName("block_status")
            @Expose
            var blockStatus: BlockStatus = BlockStatus()
    )

    data class BlockStatus(

            @SerializedName("is_blocked")
            @Expose
            var isBlocked: Boolean = false,
            @SerializedName("is_promo_blocked")
            @Expose
            var isPromoBlocked: Boolean = false,
            @SerializedName("blocked_until")
            @Expose
            var blockedUntil: String = ""
    )
}
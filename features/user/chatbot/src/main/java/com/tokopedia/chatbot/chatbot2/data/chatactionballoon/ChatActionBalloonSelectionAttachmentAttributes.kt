package com.tokopedia.chatbot.chatbot2.data.chatactionballoon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionBalloonSelectionAttachmentAttributes {
    @SerializedName("new_button_actions")
    @Expose
    val chatActions: List<ChatActionPojo> = ArrayList()

    @SerializedName("is_typing_blocked_on_button_select")
    val isTypingBlockedOnButtonSelect: Boolean = false
}

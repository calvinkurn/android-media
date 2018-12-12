package com.tokopedia.chatbot.domain.pojo.chatactionballoon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionBalloonSelectionAttachmentAttributes {
    @SerializedName("new_button_actions")
    @Expose
    val chatActions: List<ChatActionPojo> = ArrayList()
}

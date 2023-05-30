package com.tokopedia.chatbot.chatbot2.data.dynamicAttachment

import com.google.gson.annotations.SerializedName
import com.tokopedia.chatbot.chatbot2.data.chatactionballoon.ChatActionPojo

data class DynamicStickyButton(
    @SerializedName("button_action")
    val buttonAction: ChatActionPojo,
    @SerializedName("text_message")
    val textMessage: String
)

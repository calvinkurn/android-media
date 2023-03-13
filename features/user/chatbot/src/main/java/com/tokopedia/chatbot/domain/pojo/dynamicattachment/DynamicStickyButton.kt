package com.tokopedia.chatbot.domain.pojo.dynamicattachment

import com.google.gson.annotations.SerializedName
import com.tokopedia.chatbot.domain.pojo.chatactionballoon.ChatActionPojo

data class DynamicStickyButton(
    @SerializedName("button_action")
    val buttonAction: ChatActionPojo,
    @SerializedName("text_message")
    val textMessage: String
)

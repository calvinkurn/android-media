package com.tokopedia.topchat.chatlist.domain.pojo.pinchat


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatlist.domain.pojo.pinchat.ChatPin

data class PinChatResponse(
        @SerializedName("chatPin")
        val chatPin: ChatPin = ChatPin()
)
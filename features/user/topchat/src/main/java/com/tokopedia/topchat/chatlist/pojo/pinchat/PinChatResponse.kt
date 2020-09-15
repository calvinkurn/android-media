package com.tokopedia.topchat.chatlist.pojo.pinchat


import com.google.gson.annotations.SerializedName

data class PinChatResponse(
        @SerializedName("chatPin")
        val chatPin: ChatPin = ChatPin()
)
package com.tokopedia.topchat.chatroom.domain.pojo.background


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatBackgroundResponse(
        @SerializedName("chatBackground")
        @Expose
        val chatBackground: ChatBackground = ChatBackground()
)
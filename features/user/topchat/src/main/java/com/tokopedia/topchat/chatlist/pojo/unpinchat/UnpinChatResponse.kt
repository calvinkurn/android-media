package com.tokopedia.topchat.chatlist.pojo.unpinchat


import com.google.gson.annotations.SerializedName

data class UnpinChatResponse(
        @SerializedName("chatUnpin")
        val chatUnpin: ChatUnpin = ChatUnpin()
)
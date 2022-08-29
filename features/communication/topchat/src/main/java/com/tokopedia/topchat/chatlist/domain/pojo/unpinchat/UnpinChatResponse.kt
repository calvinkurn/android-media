package com.tokopedia.topchat.chatlist.domain.pojo.unpinchat


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatlist.domain.pojo.unpinchat.ChatUnpin

data class UnpinChatResponse(
        @SerializedName("chatUnpin")
        val chatUnpin: ChatUnpin = ChatUnpin()
)
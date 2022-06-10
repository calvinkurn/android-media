package com.tokopedia.topchat.chatlist.domain.pojo


import com.google.gson.annotations.SerializedName

data class ChatChangeStateResponse(
    @SerializedName(value = "", alternate = ["chatMarkRead", "chatMarkUnread"])
    val chatState: ChatState = ChatState()
)
package com.tokopedia.topchat.chatlist.pojo


import com.google.gson.annotations.SerializedName

data class ChatMarkAsReadResponse(
    @SerializedName(value = "", alternate = ["chatMarkRead", "chatMarkUnread"])
    val chatMarkRead: ChatMarkRead = ChatMarkRead()
)
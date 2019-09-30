package com.tokopedia.topchat.chatlist.pojo


import com.google.gson.annotations.SerializedName

data class ChatState(
    @SerializedName("list")
    val list: List<ChatStateItem> = emptyList()
)
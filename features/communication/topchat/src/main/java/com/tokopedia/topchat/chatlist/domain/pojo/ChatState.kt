package com.tokopedia.topchat.chatlist.domain.pojo

import com.google.gson.annotations.SerializedName

data class ChatState(
    @SerializedName("list")
    val list: List<ChatStateItem> = emptyList()
)
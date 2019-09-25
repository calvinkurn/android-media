package com.tokopedia.topchat.chatlist.pojo


import com.google.gson.annotations.SerializedName

data class ChatMarkRead(
    @SerializedName("list")
    val list: MarkAsReadItem = MarkAsReadItem()
)
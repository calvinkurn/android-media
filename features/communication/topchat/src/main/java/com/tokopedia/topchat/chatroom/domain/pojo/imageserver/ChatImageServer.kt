package com.tokopedia.topchat.chatroom.domain.pojo.imageserver

import com.google.gson.annotations.SerializedName

data class ChatImageServer(
    @SerializedName("sourceID")
    val sourceID: String = "",

    @SerializedName("sourceIDSecure")
    val sourceIDSecure: String = ""
)

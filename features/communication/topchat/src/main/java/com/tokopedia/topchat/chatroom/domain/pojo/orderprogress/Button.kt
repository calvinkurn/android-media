package com.tokopedia.topchat.chatroom.domain.pojo.orderprogress


import com.google.gson.annotations.SerializedName

data class Button(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("label")
        val label: String = "",
        @SerializedName("uri")
        val uri: String = ""
)
package com.tokopedia.topchat.chatroom.domain.pojo.orderprogress


import com.google.gson.annotations.SerializedName

data class Label(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("value")
        val value: String = ""
)
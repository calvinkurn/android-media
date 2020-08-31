package com.tokopedia.topchat.chatroom.domain.pojo.chatattachment


import com.google.gson.annotations.SerializedName

data class Fallback(
        @SerializedName("html")
        val html: String = "",
        @SerializedName("message")
        val message: String = ""
)
package com.tokopedia.topchat.chatroom.domain.pojo.background


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatBackground(
        @SerializedName("urlImage")
        @Expose
        val urlImage: String = "",
        @SerializedName("urlImageDarkMode")
        @Expose
        val urlImageDarkMode: String = ""
)
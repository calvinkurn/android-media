package com.tokopedia.topchat.chatsetting.data

import com.google.gson.annotations.SerializedName

data class ChatSetting(
        @SerializedName("alias")
        val alias: String = "",
        @SerializedName("link")
        val link: String = ""
)
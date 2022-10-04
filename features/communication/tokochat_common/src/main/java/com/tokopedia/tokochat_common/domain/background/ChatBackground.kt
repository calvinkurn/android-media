package com.tokopedia.tokochat_common.domain.background


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatBackground(
        @SerializedName("urlImage")
        @Expose
        var urlImage: String = "",
        @SerializedName("urlImageDarkMode")
        @Expose
        val urlImageDarkMode: String = ""
)

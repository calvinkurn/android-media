package com.tokopedia.tokochat.domain.response.background


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoChatBackground(
        @SerializedName("urlImage")
        @Expose
        var urlImage: String = "",
        @SerializedName("urlImageDarkMode")
        @Expose
        val urlImageDarkMode: String = ""
)

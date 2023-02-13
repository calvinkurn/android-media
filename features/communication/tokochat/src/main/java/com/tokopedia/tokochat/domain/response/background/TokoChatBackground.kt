package com.tokopedia.tokochat.domain.response.background

import com.google.gson.annotations.SerializedName

data class TokoChatBackground(
        @SerializedName("urlImage")
        var urlImage: String = "",
        @SerializedName("urlImageDarkMode")
        val urlImageDarkMode: String = ""
)

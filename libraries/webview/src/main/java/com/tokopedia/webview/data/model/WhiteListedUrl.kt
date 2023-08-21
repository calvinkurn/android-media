package com.tokopedia.webview.data.model

import com.google.gson.annotations.SerializedName

data class WhiteListedUrl(
    @SerializedName("isEnabled")
    val isEnabled: Boolean = false,
    @SerializedName("urls")
    val urls: List<String> = emptyList()
)

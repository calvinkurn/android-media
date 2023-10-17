package com.tokopedia.webview.data.model

import com.google.gson.annotations.SerializedName

data class WhiteListedPath(
    @SerializedName("isEnabled")
    val isEnabled: Boolean = false,
    @SerializedName("whitelistedPrefix")
    val path: List<String> = emptyList()
)

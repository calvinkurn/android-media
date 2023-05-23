package com.tokopedia.webview.data.model

import com.google.gson.annotations.SerializedName

data class WhiteListedFintechPath(
    @SerializedName("isEnabled")
    val isEnabled: Boolean = false,
    @SerializedName("whitelistedPrefix")
    val path: List<String> = emptyList()
)

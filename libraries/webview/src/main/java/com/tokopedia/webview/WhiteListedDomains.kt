package com.tokopedia.webview

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WhiteListedDomains(
        @SerializedName("isEnabled")
        @Expose
        val isEnabled: Boolean = false,
        @SerializedName("whitelistedUrls")
        @Expose
        val domains: List<String> = emptyList()
)
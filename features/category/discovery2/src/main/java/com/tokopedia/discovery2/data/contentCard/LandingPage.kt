package com.tokopedia.discovery2.data.contentCard

import com.google.gson.annotations.SerializedName

data class LandingPage(
    @SerializedName("header_subtitle")
    val headerSubtitle: String? = "",

    @SerializedName("url")
    val url: String? = "",

    @SerializedName("applink")
    val appLink: String? = ""
)

package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class LihatSemua(
        @SerializedName("applink")
        var applink: String = "",
        @SerializedName("header")
        var header: String = "",
        @SerializedName("mobile_url")
        var mobileUrl: String = "",
        @SerializedName("subheader")
        var subheader: String = "",
        @SerializedName("url")
        var url: String = ""
)
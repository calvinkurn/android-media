package com.tokopedia.attachvoucher.data


import com.google.gson.annotations.SerializedName

data class Banner(
        @SerializedName("desktop_url")
        val desktopUrl: String = "",
        @SerializedName("mobile_url")
        val mobileUrl: String = ""
)
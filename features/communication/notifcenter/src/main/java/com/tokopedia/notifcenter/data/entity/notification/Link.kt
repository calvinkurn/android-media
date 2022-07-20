package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Link(
        @SerializedName("android_link")
        val androidLink: String = "",
        @SerializedName("desktop_link")
        val desktopLink: String = "",
        @SerializedName("ios_link")
        val iosLink: String = "",
        @SerializedName("mobile_link")
        val mobileLink: String = ""
)
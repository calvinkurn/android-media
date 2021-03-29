package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Widget(
    @SerializedName("android_button_link")
    val androidButtonLink: String = "",
    @SerializedName("button_text")
    val buttonText: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("desktop_button_link")
    val desktopButtonLink: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("ios_button_link")
    val iosButtonLink: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("mobile_button_link")
    val mobileButtonLink: String = "",
    @SerializedName("title")
    val title: String = ""
)
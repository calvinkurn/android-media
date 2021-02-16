package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class ButtonCta(
    @SerializedName("data")
    val `data`: String = "",
    @SerializedName("link")
    val link: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("type")
    val type: Int = 0
)
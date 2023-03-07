package com.tokopedia.home.beranda.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SubscriptionsTextAttributes(
    @SerializedName("Color")
    @Expose
    val color: String = "",
    @SerializedName("IsBold")
    @Expose
    val isBold: Boolean = false,
    @SerializedName("Text")
    @Expose
    val text: String = ""
)
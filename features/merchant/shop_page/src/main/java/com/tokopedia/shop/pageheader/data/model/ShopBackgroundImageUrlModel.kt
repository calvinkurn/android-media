package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.SerializedName

data class ShopBackgroundImageUrlModel(
    @SerializedName("dark_mode")
    val darkModeUrl: String = "",
    @SerializedName("light_mode")
    val lightModeUrl: String = ""
)

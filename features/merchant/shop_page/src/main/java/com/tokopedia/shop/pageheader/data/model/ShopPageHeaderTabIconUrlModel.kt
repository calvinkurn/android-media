package com.tokopedia.shop.pageheader.data.model

import com.google.gson.annotations.SerializedName

data class ShopPageHeaderTabIconUrlModel(
    @SerializedName("dark_mode")
    val darkModeUrl: String = "",
    @SerializedName("light_mode")
    val lightModeUrl: String = "",
    @SerializedName("dark_theme")
    val darkThemeUrl: String = ""
)

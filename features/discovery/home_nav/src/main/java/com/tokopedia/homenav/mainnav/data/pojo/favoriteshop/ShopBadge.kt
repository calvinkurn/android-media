package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopBadge(
    @SerializedName("title")
    @Expose
    val title: String? = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String? = ""
)

package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavoriteShops(
    @SerializedName("shops")
    @Expose
    val shops: List<Shop>? = listOf()
)

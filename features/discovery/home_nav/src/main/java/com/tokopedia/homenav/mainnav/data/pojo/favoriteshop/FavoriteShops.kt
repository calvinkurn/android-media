package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavoriteShops(
    @SerializedName("totalCount")
    @Expose
    val totalCount: Int? = 0,
    @SerializedName("userShopFollowDetail")
    @Expose
    val shops: List<Shop>? = listOf()
)

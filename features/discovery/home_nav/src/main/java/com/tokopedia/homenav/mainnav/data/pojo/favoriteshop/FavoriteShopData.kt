package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavoriteShopData(
    @SerializedName("favorite_shop")
    @Expose
    val favoriteShops: FavoriteShops? = FavoriteShops()
)

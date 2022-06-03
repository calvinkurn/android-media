package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FavoriteShopData(
    @SerializedName("userShopFollow")
    @Expose
    val userShopFollow: FavoriteShopResult? = FavoriteShopResult()
){
    data class FavoriteShopResult(
        @SerializedName("result")
        @Expose
        val favoriteShops: FavoriteShops? = FavoriteShops()
    )
}

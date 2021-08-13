package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ShopTotalFollowers(
        @SerializedName("shopInfoByID")
        @Expose
        val shopInfoById: ShopInfoById? = ShopInfoById()
)

data class ShopInfoById(
        @SerializedName("result")
        @Expose
        val result: List<Result>? = listOf(),
        @SerializedName("error")
        @Expose
        val error: Error? = Error()
)

data class Result(
        @SerializedName("favoriteData")
        @Expose
        val favoriteData: FavoriteData? = FavoriteData()
)

data class FavoriteData(
        @SerializedName("totalFavorite")
        @Expose
        val totalFavorite: Long? = 0
)

data class Error(
        @SerializedName("message")
        @Expose
        val message: String? = ""
)
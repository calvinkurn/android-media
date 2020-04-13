package com.tokopedia.sellerhome.settings.domain.entity

import com.google.gson.annotations.SerializedName


data class ShopTotalFollowers(
        @SerializedName("shopInfoByID")
        val shopInfoById: ShopInfoById = ShopInfoById()
)

data class ShopInfoById(
        @SerializedName("result")
        val result: List<Result> = listOf(),
        @SerializedName("error")
        val error: Error = Error()
)

data class Result(
        @SerializedName("favoriteData")
        val favoriteData: FavoriteData = FavoriteData()
)

data class FavoriteData(
        @SerializedName("totalFavorite")
        val totalFavorite: Int = 0
)

data class Error(
        @SerializedName("message")
        val message: String = ""
)
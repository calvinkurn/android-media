package com.tokopedia.product.detail.data.model.shop


import com.google.gson.annotations.SerializedName

data class ProductShopFollowResponse(
        @SerializedName("shopInfoByID")
        val shopInfoByID: ShopInfoByID = ShopInfoByID()
)

data class ShopInfoByID(
        @SerializedName("result")
        val result: List<Result> = listOf()
)

data class Result(
        @SerializedName("favoriteData")
        val favoriteData: FavoriteData = FavoriteData()
)

data class FavoriteData(
        @SerializedName("alreadyFavorited")
        val alreadyFavorited: Int = 0
)



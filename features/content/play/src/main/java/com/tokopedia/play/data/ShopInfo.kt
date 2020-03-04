package com.tokopedia.play.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-10.
 */
data class ShopInfo(
        @SerializedName("shopCore")
        val shopCore: ShopCore = ShopCore(),
        @SerializedName("favoriteData")
        val favoriteData: FavoriteData = FavoriteData()
) {

    data class Response(
            @SerializedName("shopInfoByID")
            val result: Result = Result()
    )

    data class Result(
            @SerializedName("result")
            @Expose
            val data: List<ShopInfo> = listOf(),
            @SerializedName("error")
            val error: Error = Error()
    )

    data class ShopCore(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("shopID")
            val shopId: String = ""
    )

    data class FavoriteData(
            @SerializedName("totalFavorite")
            val totalFavorite: Int = 0,
            @SerializedName("alreadyFavorited")
            val alreadyFavorited: Int = 0
    )

    data class Error(
            @SerializedName("message")
            val message: String = ""
    )
}
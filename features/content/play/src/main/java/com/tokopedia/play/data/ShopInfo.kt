package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-10.
 */
data class ShopInfo(
        @SerializedName("result")
        val result: List<Result> = emptyList(),

        @SerializedName("error")
        val error: Error = Error()

) {

    data class Data(
            @SerializedName("data")
            val data: Response = Response()
    )

    data class Response(
            @SerializedName("shopInfoByID")
            val shopInfo: ShopInfo = ShopInfo()
    )

    data class Result(
            @SerializedName("shopCore")
            val shopCore: ShopCore = ShopCore(),
            @SerializedName("favoriteData")
            val favoriteData: FavoriteData = FavoriteData()

    ) {
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
    }

    data class Error(
            @SerializedName("message")
            val message: String = ""
    )
}
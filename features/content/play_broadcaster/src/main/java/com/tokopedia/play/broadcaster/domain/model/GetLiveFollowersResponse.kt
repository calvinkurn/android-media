package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 12/06/20
 */
data class GetLiveFollowersResponse(
        @SerializedName("shopFollowerList")
        val shopFollowerList: ShopFollowerList,

        @SerializedName("shopInfoByID")
        val shopInfoById: ShopInfoByID
) {

    data class ShopFollowerList(
            @SerializedName("data")
            val data: List<Data>
    ) {
        data class Data(
                @SerializedName("photo")
                val photo: String
        )
    }

    data class ShopInfoByID(
            @SerializedName("result")
            val result: List<Result>
    ) {
        data class Result(
                @SerializedName("favoriteData")
                val favoriteData: FavoriteData
        )

        data class FavoriteData(
                @SerializedName("totalFavorite")
                val totalFavorite: Int
        )
    }
}
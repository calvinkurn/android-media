package com.tokopedia.topchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 16/01/19.
 */
data class ShopFollowingPojo(
        @SerializedName("shopInfoByID")
        @Expose
        var shopInfoById: ShopInfoById = ShopInfoById()
)

data class ShopInfoById(
        @SerializedName("result")
        @Expose
        var result: ArrayList<ResultItem> = ArrayList()
)

data class ResultItem(
        @SerializedName("favoriteData")
        @Expose
        var favoriteData: FavoriteData = FavoriteData()
)

data class FavoriteData(
        @SerializedName("alreadyFavorited")
        @Expose
        var alreadyFavorited: Int = 0
)
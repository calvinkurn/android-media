package com.tokopedia.homenav.mainnav.data.pojo.favoriteshop

import com.google.gson.annotations.SerializedName

data class FavoriteShopParam(
    @SerializedName("page")
    var page: Int = 1,

    @SerializedName("perPage")
    var perPage: Int = 5,

    @SerializedName("userID")
    var userId: String = ""
)
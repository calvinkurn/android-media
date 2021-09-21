package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoQuality {
    @SerializedName("average")
    @Expose
    var average: String? = null

    @SerializedName("count_total")
    @Expose
    var countTotal: String? = null

    @SerializedName("five_star_rank")
    @Expose
    var fiveStarRank: String? = null

    @SerializedName("four_star_rank")
    @Expose
    var fourStarRank: String? = null

    @SerializedName("one_star_rank")
    @Expose
    var oneStarRank: String? = null

    @SerializedName("rating_star")
    @Expose
    var ratingStar = 0f

    @SerializedName("three_star_rank")
    @Expose
    var threeStarRank: String? = null

    @SerializedName("two_star_rank")
    @Expose
    var twoStarRank: String? = null
}
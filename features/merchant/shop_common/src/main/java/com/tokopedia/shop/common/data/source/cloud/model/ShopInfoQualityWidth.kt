package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoQualityWidth {
    @SerializedName("five_star_rank")
    @Expose
    var fiveStarRank = 0.0

    @SerializedName("four_star_rank")
    @Expose
    var fourStarRank = 0.0

    @SerializedName("one_star_rank")
    @Expose
    var oneStarRank = 0.0

    @SerializedName("three_star_rank")
    @Expose
    var threeStarRank = 0.0

    @SerializedName("two_star_rank")
    @Expose
    var twoStarRank = 0.0
}
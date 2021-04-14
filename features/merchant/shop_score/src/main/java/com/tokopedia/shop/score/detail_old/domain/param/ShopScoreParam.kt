package com.tokopedia.shop.score.detail_old.domain.param

import com.google.gson.annotations.SerializedName

data class ShopScoreParam(
    @SerializedName("shopID")
    val shopId: String
)
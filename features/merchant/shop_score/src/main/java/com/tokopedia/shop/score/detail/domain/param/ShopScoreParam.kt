package com.tokopedia.shop.score.detail.domain.param

import com.google.gson.annotations.SerializedName

data class ShopScoreParam(
    @SerializedName("shopID")
    val shopId: String
)
package com.tokopedia.shop.score.detail.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreResponse(
    @SerializedName("shopScore")
    val data: ShopScoreData = ShopScoreData()
)
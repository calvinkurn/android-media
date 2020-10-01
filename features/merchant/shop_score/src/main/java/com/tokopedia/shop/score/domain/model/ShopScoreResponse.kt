package com.tokopedia.shop.score.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreResponse(
    @SerializedName("shopScore")
    val response: ShopScoreData
)
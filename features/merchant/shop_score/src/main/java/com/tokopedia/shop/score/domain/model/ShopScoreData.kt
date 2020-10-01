package com.tokopedia.shop.score.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreData(
    @SerializedName("result")
    val result: ShopScoreResult,
    @SerializedName("error")
    val error: ShopScoreError?
)
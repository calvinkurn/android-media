package com.tokopedia.shop.score.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreError(
    @SerializedName("message")
    val message: String
)
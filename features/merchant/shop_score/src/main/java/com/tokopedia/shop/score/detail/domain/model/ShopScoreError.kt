package com.tokopedia.shop.score.detail.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreError(
    @SerializedName("message")
    val message: String
)
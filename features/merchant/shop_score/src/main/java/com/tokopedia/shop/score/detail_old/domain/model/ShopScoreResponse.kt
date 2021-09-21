package com.tokopedia.shop.score.detail_old.domain.model

import com.google.gson.annotations.SerializedName

data class ShopScoreResponse(
    @SerializedName("shopScore")
    val data: ShopScoreData = ShopScoreData()
)
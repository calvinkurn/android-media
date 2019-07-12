package com.tokopedia.shop.common.graphql.data.shopscore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScoreResponse(
        @SerializedName("shopScore")
        @Expose
        val shopScore: ShopScore = ShopScore()
)
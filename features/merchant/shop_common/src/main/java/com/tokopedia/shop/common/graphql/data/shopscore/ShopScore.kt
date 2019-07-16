package com.tokopedia.shop.common.graphql.data.shopscore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopScore(
        @SerializedName("result")
        @Expose
        val result: Result = Result(),
        @SerializedName("error")
        @Expose
        val error: ShopScoreError = ShopScoreError()
)
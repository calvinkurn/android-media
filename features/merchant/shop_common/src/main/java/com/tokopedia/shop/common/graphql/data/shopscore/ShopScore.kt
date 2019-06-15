package com.tokopedia.shop.common.graphql.data.shopscore

import com.google.gson.annotations.SerializedName

data class ShopScore(
        @SerializedName("result")
        val result: Result = Result()
)
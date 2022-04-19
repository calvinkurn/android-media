package com.tokopedia.shop.common.graphql.data.shopinfobydomain


import com.google.gson.annotations.SerializedName

data class ShopInfoByID(
    @SerializedName("result")
    val result: List<Result> = listOf()
)
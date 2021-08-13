package com.tokopedia.shop.common.graphql.data.shopinfobydomain


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("shopCore")
    val shopCore: ShopCore = ShopCore()
)
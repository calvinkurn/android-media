package com.tokopedia.shop.common.graphql.data.shopinfobydomain


import com.google.gson.annotations.SerializedName

data class ShopCore(
    @SerializedName("shopID")
    val shopID: String = ""
)
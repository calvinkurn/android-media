package com.tokopedia.shop.common.graphql.data.shopinfobydomain


import com.google.gson.annotations.SerializedName

data class ShopInfoByDomainResponse(
    @SerializedName("shopInfoByID")
    val shopInfoByID: ShopInfoByID = ShopInfoByID()
)
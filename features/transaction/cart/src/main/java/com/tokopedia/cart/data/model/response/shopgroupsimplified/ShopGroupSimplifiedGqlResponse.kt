package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ShopGroupSimplifiedGqlResponse(
    @SerializedName("cart_revamp_v4")
    val shopGroupSimplifiedResponse: ShopGroupSimplifiedResponse = ShopGroupSimplifiedResponse()
)

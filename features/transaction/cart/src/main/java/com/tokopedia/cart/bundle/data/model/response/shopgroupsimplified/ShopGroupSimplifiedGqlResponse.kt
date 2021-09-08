package com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ShopGroupSimplifiedGqlResponse(
        @SerializedName("cart_revamp_v3")
        val shopGroupSimplifiedResponse: ShopGroupSimplifiedResponse = ShopGroupSimplifiedResponse()
)
package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-18.
 */

data class ShopGroupSimplifiedGqlResponse(
        @SerializedName("cart_revamp")
        val shopGroupSimplifiedResponse: ShopGroupSimplifiedResponse = ShopGroupSimplifiedResponse()
)
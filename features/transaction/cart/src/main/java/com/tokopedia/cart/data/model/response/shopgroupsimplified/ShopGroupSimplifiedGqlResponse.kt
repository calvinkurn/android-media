package com.tokopedia.cart.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-18.
 */

data class ShopGroupSimplifiedGqlResponse(
        @SerializedName("shop_group_simplified")
        val shopGroupSimplifiedResponse: ShopGroupSimplifiedResponse
)
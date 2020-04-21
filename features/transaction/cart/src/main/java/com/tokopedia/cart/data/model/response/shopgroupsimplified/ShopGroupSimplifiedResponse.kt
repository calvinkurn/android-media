package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-18.
 */

data class ShopGroupSimplifiedResponse(
        @SerializedName("status")
        val status: String = "",

        @SerializedName("error_message")
        val errorMessages: List<String> = emptyList(),

        @SerializedName("data")
        val data: CartDataListResponse = CartDataListResponse()
)
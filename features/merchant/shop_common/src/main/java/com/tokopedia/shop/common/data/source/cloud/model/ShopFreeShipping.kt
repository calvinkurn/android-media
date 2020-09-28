package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

class ShopFreeShipping(
    @SerializedName("EligibleServiceShop")
    val response: Response
) {

    data class Response(
        @SerializedName("shops")
        val shops: List<Shop>
    )

    data class Shop(
        @SerializedName("shopID")
        val shopId:  Int,
        @SerializedName("status")
        val status: Boolean,
        @SerializedName("statusEligible")
        val statusEligible: Boolean
    )
}
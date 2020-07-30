package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

data class ShopInfoFreeShipping(
    @SerializedName("shopInfoByID")
    val response: Response
) {

    data class Response(
        @SerializedName("result")
        val result: List<FreeShippingInfo>
    )

    data class FreeShippingInfo(
        @SerializedName("freeOngkir")
        val freeShipping: FreeOngkir
    )
}
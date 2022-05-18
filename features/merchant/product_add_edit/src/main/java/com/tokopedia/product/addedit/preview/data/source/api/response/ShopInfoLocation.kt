package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class ShopInfoById(
        @SerializedName("result")
        val result: List<Shipping>
)

data class Shipping(
        @SerializedName("shippingLoc")
        val shippingLoc: ShippingLoc
)

data class ShippingLoc(
        @SerializedName("provinceID")
        val provinceId: Int
)
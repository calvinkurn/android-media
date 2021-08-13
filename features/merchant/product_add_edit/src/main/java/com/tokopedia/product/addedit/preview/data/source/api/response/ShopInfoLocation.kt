package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopInfoById(
        @Expose
        @SerializedName("result")
        val result: List<Shipping>
)

data class Shipping(
        @Expose
        @SerializedName("shippingLoc")
        val shippingLoc: ShippingLoc
)

data class ShippingLoc(
        @Expose
        @SerializedName("provinceID")
        val provinceId: Int
)
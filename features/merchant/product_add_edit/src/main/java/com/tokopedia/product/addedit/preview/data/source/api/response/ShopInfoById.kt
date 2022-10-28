package com.tokopedia.product.addedit.preview.data.source.api.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ShopInfoById(
    @SerializedName("result")
    val result: List<Shipping> = arrayListOf()
)

data class Shipping(
    @SerializedName("shippingLoc")
    val shippingLoc: ShippingLoc = ShippingLoc()
)

data class ShippingLoc(
    @SuppressLint("Invalid Data Type") // provinceId currently using Integer at server
    @SerializedName("provinceID")
    val provinceId: Int = 0
)

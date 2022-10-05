package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class ShopInfo(
    @SerializedName("shippingLoc")
    val shippingLoc: ShippingLoc,
    @SerializedName("statusInfo")
    val statusInfo: StatusInfo
)

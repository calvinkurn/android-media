package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class GroupShopV2(
    @SerializedName("cart_string_order")
    val cartStringOrder: String = "",
    @SerializedName("shop")
    val shop: Shop = Shop(),
    @SerializedName("cart_details")
    val cartDetails: List<CartDetail> = emptyList()
)

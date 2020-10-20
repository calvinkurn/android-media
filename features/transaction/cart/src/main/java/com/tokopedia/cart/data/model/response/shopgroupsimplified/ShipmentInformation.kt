package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ShipmentInformation(
        @SerializedName("shop_location")
        val shopLocation: String = "",
        @SerializedName("estimation")
        val estimation: String = "",
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("preorder")
        val preorder: PreOrder = PreOrder()
)
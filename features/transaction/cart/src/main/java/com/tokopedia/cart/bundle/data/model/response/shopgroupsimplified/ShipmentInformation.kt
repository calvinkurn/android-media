package com.tokopedia.cart.bundle.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class ShipmentInformation(
        @SerializedName("shop_location")
        val shopLocation: String = "",
        @SerializedName("estimation")
        val estimation: String = "",
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("free_shipping_extra")
        val freeShippingExtra: FreeShipping = FreeShipping(),
        @SerializedName("preorder")
        val preorder: PreOrder = PreOrder(),
        @SerializedName("enable_bo_affordability")
        val enableBoAffordability: Boolean = false
)
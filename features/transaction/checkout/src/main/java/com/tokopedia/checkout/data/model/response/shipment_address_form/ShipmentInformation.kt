package com.tokopedia.checkout.data.model.response.shipment_address_form

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
        val preorder: Preorder = Preorder()
)
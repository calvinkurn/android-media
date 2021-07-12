package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class ShipmentInformationResponse(
        @SerializedName("shop_location")
        val shopLocation: String = "",
        @SerializedName("estimation")
        val estimation: String = "",
        @SerializedName("free_shipping")
        val freeShipping: FreeShipping = FreeShipping(),
        @SerializedName("free_shipping_extra")
        val freeShippingExtra: FreeShipping = FreeShipping(),
        @SerializedName("preorder")
        val preorder: PreOrder = PreOrder()
)

class FreeShipping(
        @SerializedName("eligible")
        val eligible: Boolean = false,
        @SerializedName("badge_url")
        val badgeUrl: String = ""
)

class PreOrder(
        @SerializedName("is_preorder")
        val isPreorder: Boolean = false,
        @SerializedName("duration")
        val duration: String = ""
)
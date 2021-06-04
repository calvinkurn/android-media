package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class UnavailableGroup(
        @SerializedName("shipment_information")
        val shipmentInformation: ShipmentInformation = ShipmentInformation(),
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("cart_string")
        val cartString: String = "",
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("cart_details")
        val cartDetails: List<CartDetail> = emptyList()
)
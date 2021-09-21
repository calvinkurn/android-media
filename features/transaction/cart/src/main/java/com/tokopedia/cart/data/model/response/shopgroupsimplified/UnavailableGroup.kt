package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class UnavailableGroup(
        @SerializedName("shipment_information")
        val shipmentInformation: ShipmentInformation = ShipmentInformation(),
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("is_fulfillment_service")
        val isFulFillment: Boolean = false,
        @SerializedName("warehouse")
        val warehouse: Warehouse = Warehouse(),
        @SerializedName("cart_details")
        val cartDetails: List<CartDetail> = emptyList(),
        @SerializedName("total_cart_details_error")
        val totalCartDetailsError: Int = 0,
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("sort_key")
        val sortKey: Long = 0,
        @SerializedName("cart_string")
        val cartString: String = ""
)
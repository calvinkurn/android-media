package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class AvailableGroup(
        @SerializedName("cart_details")
        val cartDetails: List<CartDetail> = emptyList(),
        @SerializedName("cart_string")
        val cartString: String = "",
        @SerializedName("checkbox_state")
        val checkboxState: Boolean = false,
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("is_fulfillment_service")
        val isFulfillmentService: Boolean = false,
        @SerializedName("shipment_information")
        val shipmentInformation: ShipmentInformation = ShipmentInformation(),
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("sort_key")
        val sortKey: String = "",
        @SerializedName("total_cart_details_error")
        val totalCartDetailsError: Int = 0,
        @SerializedName("total_cart_price")
        val totalCartPrice: Long = 0L,
        @SerializedName("user_address_id")
        val userAddressId: String = "",
        @SerializedName("warehouse")
        val warehouse: Warehouse = Warehouse()
)
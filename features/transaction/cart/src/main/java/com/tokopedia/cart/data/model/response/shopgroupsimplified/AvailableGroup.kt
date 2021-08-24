package com.tokopedia.cart.data.model.response.shopgroupsimplified

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class AvailableGroup(
        @SuppressLint("Invalid Data Type")
        @SerializedName("user_address_id")
        val userAddressId: String = "",
        @SerializedName("shipment_information")
        val shipmentInformation: ShipmentInformation = ShipmentInformation(),
        @SerializedName("cart_string")
        val cartString: String = "",
        @SerializedName("pinned")
        val pinned: Pinned = Pinned(),
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("is_fulfillment_service")
        val isFulfillment: Boolean = false,
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
        @SerializedName("has_promo_list")
        val hasPromoList: Boolean = false,
        @SerializedName("checkbox_state")
        val checkboxState: Boolean = false,
        @SerializedName("promo_codes")
        val promoCodes: List<String> = emptyList()
)
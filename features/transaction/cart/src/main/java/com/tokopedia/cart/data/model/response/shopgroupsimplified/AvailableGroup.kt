package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

data class AvailableGroup(
        @SerializedName("user_address_id")
        val userAddressId: Int = 0,
        @SerializedName("shipment_information")
        val shipmentInformation: ShipmentInformation = ShipmentInformation(),
        @SerializedName("cart_string")
        val cartString: String = "",
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
        @SerializedName("has_promo_list")
        val hasPromoList: Boolean = false,
        @SerializedName("checkbox_state")
        val checkboxState: Boolean = false,
        @SerializedName("promo_codes")
        val promoCodes: List<String> = emptyList()
)
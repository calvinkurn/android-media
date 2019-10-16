package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

data class ShopGroupWithError(
        @SerializedName("user_address_id")
        val userAddressId: Int = 0,

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
        val sortKey: Int = 0

)
package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-13.
 */
data class VehicleLeasing (
        @field:SerializedName("application_id")
        val applicationId: Int = 0,

        @field:SerializedName("dp_price")
        val dpPrice: Int = 0,

        @field:SerializedName("booking_fee")
        val bookingFee: Int = 0,

        @field:SerializedName("total_price")
        val totalPrice: Int = 0,

        @field:SerializedName("product_id")
        val productId: Int = 0,

        @field:SerializedName("dealer_id")
        val dealerId: Int = 0,

        @field:SerializedName("multifinance_name")
        val multifinanceName: String = "",

        @field:SerializedName("is_leasing_product")
        val isLeasingProduct: Boolean = false,

        @field:SerializedName("is_allow_checkout")
        val isAllowCheckout: Boolean = false,

        @field:SerializedName("error_message")
        val errorMessage: String = "",

        @field:SerializedName("original_price")
        val originalPrice: Int = 0
)
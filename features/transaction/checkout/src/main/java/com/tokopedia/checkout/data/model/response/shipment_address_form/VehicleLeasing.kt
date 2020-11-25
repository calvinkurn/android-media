package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-08-13.
 */
data class VehicleLeasing (
        @field:SerializedName("booking_fee")
        val bookingFee: Int = 0,

        @field:SerializedName("is_leasing_product")
        val isLeasingProduct: Boolean = false
)
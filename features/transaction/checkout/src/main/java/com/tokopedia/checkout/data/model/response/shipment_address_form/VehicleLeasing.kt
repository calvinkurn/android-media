package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class VehicleLeasing(
        @SerializedName("booking_fee")
        val bookingFee: Int = 0,
        @SerializedName("is_leasing_product")
        val isLeasingProduct: Boolean = false
)
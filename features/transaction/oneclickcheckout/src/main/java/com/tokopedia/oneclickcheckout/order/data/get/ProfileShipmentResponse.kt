package com.tokopedia.oneclickcheckout.order.data.get

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

class Shipment(
        @SuppressLint("Invalid Data Type")
        @SerializedName("service_id")
        val serviceId: Int = 0,
        @SerializedName("service_duration")
        val serviceDuration: String = "",
        @SerializedName("service_name")
        val serviceName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        val spId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("recommendation_service_id")
        val recommendationServiceId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("recommendation_sp_id")
        val recommendationSpId: Int = 0,
        @SerializedName("is_free_shipping_selected")
        val isFreeShippingSelected: Boolean = false
)

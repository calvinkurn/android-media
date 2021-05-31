package com.tokopedia.oneclickcheckout.common.data.model

import com.google.gson.annotations.SerializedName

data class Shipment(
        @SerializedName("service_name")
        val serviceName: String = "",
        @SerializedName("service_id")
        val serviceId: Int = 0,
        @SerializedName("service_duration")
        val serviceDuration: String = "",
        @SerializedName("sp_id")
        val spId: Int = 0,
        @SerializedName("recommendation_service_id")
        val recommendationServiceId: Int = 0,
        @SerializedName("recommendation_sp_id")
        val recommendationSpId: Int = 0,
        @SerializedName("is_free_shipping_selected")
        val isFreeShippingSelected: Boolean = false,
        @SerializedName("estimation")
        val estimation: String = ""
)

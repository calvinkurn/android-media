package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class Shipment(
        @SerializedName("service_id")
        val serviceId: String = "",
        @SerializedName("service_duration")
        val serviceDuration: String = "",
        @SerializedName("service_name")
        val serviceName: String = "",
        @SerializedName("sp_id")
        val spId: String = "",
        @SerializedName("recommendation_service_id")
        val recommendationServiceId: String = "",
        @SerializedName("recommendation_sp_id")
        val recommendationSpId: String = "",
        @SerializedName("is_free_shipping_selected")
        val isFreeShippingSelected: Boolean = false
)

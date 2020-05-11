package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference

import com.google.gson.annotations.SerializedName

data class Shipment(
        @SerializedName("service_name")
        val serviceName: String = "",
        @SerializedName("service_id")
        val serviceId: Int = 0,
        @SerializedName("service_duration")
        val serviceDuration: String = ""
)

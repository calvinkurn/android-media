package com.tokopedia.oneclickcheckout.common.data.model

import com.google.gson.annotations.SerializedName

data class Shipment(
        @SerializedName("service_name")
        val serviceName: String = "",
        @SerializedName("service_id")
        val serviceId: Int = 0,
        @SerializedName("service_duration")
        val serviceDuration: String = ""
)

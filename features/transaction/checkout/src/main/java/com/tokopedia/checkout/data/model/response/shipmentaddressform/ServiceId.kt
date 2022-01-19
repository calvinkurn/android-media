package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ServiceId(
        @SerializedName("service_id")
        val serviceId: String = "",
        @SerializedName("sp_ids")
        val spIds: String = ""
)
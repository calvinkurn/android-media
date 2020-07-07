package com.tokopedia.oneclickcheckout.preference.edit.data.shipping

import com.google.gson.annotations.SerializedName

data class ShippingNoPriceResponse (
        @SerializedName("ongkir_shipper_service")
        var response: Response
)

data class Response(
        @SerializedName("services")
        var services: List<Services> = emptyList()
)

data class Services (
        @SerializedName("service_code")
        var serviceCode: String = "",
        @SerializedName("service_id")
        var serviceId: Int = 0,
        @SerializedName("service_duration")
        var serviceDuration: String = "",
        @SerializedName("shipper_ids")
        var shipperIds: List<Int> = emptyList(),
        @SerializedName("spids")
        var spIds: List<Int> = emptyList()
)

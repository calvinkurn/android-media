package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ShipmentSummaryAddOn(
    @SerializedName("wording")
    val wording: String = "",
    @SerializedName("type")
    val type: Int = -1
)
package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ShipmentAction(
    @SerializedName("sp_id")
    val spId: Long = 0,
    @SerializedName("action")
    val action: String = ""
)

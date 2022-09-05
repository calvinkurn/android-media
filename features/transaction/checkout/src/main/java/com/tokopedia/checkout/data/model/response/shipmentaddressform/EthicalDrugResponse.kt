package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class EthicalDrugResponse(
    @SerializedName("need_prescription")
    val needPrescription: Boolean = false,
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("text")
    val text: String = ""
)

package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class EthicalDrug(
    @SerializedName("need_prescription")
    val needPrescription: Boolean = false,
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("text")
    val text: String = ""
)

package com.tokopedia.checkout.data.model.response.dynamicdata

import com.google.gson.annotations.SerializedName

data class ShipmentDynamicDataPassing(
    @SerializedName("is_ddp")
    val isDdp: Boolean = false,
    @SerializedName("dynamic_data")
    val dynamicData: String = ""
)

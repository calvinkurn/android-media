package com.tokopedia.checkout.data.model.request.saveshipmentstate

import com.google.gson.annotations.SerializedName

data class ShipmentStateDropshipData(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("telp_no")
    var telpNo: String = ""
)

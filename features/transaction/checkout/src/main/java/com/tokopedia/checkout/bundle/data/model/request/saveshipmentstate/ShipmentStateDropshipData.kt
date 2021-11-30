package com.tokopedia.checkout.bundle.data.model.request.saveshipmentstate

import com.google.gson.annotations.SerializedName

data class ShipmentStateDropshipData(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("telp_no")
        var telpNo: String? = null,
)
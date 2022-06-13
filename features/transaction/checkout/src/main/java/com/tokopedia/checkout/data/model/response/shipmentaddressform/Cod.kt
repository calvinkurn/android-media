package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class Cod(
        @SerializedName("is_cod")
        var isCod: Boolean = false,
        @SerializedName("counter_cod")
        var counterCod: Int = 0
)
package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class Cod(
        @SerializedName("is_cod")
        var isCod: Boolean = false,
        @SerializedName("counter_cod")
        var counterCod: Int = 0
)
package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FreeReturns(
        @SerializedName("free_returns_logo")
        @Expose
        var freeReturnsLogo: String = ""
)

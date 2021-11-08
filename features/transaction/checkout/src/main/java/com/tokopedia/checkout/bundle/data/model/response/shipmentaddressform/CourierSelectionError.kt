package com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class CourierSelectionError(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = ""
)

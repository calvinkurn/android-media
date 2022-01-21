package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class Addresses(
        @SerializedName("active")
        val active: String = "",
        @SerializedName("data")
        val data: List<Data> = emptyList(),
        @SerializedName("disable_tabs")
        val disableTabs: List<String> = emptyList()
)
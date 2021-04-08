package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class Donation(
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("Nominal")
        val nominal: Int = 0,
        @SerializedName("Description")
        val description: String = ""
)
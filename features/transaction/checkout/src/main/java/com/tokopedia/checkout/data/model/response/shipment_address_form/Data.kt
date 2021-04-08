package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("value")
        val value: UserAddress = UserAddress()
)
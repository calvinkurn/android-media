package com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("key")
        val key: String = "",
        @SerializedName("value")
        val value: UserAddress = UserAddress()
)
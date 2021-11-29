package com.tokopedia.checkout.old.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class DisabledFeaturesDetail(
        @SerializedName("disabled_multi_address_message")
        val disabledMultiAddressMessage: String = ""
)
package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class DisabledFeaturesDetail(
        @SerializedName("disabled_multi_address_message")
        val disabledMultiAddressMessage: String = ""
)
package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ShipmentPlatformFee(
    @SerializedName("enable")
    val isEnable: Boolean = false,
    @SerializedName("profile_code")
    val profileCode: String = "",
    @SerializedName("additional_data")
    val additionalData: String = "",
    @SerializedName("error_wording")
    val errorWording: String = ""
)

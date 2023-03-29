package com.tokopedia.checkout.data.model.response.platformfee

import com.google.gson.annotations.SerializedName

data class ShipmentDynamicPlatformFee(
    @SerializedName("is_using_platform_fee")
    val isUsingPlatformFee: Boolean = false,
    @SerializedName("profile_code")
    val profileCode: String = "",
    @SerializedName("gateway_code")
    val gatewayCode: String = "",
    @SerializedName("ticker")
    val ticker: String = ""
)

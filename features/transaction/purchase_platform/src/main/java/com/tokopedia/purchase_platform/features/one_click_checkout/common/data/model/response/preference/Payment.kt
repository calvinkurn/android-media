package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference

import com.google.gson.annotations.SerializedName

data class Payment(
        @SerializedName("gateway_code")
        val gatewayCode: String = "",
        @SerializedName("gateway_name")
        val gatewayName: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("url")
        val url: String = ""
)

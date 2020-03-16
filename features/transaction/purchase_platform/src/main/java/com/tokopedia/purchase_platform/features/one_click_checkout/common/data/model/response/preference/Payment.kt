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
        val url: String = "",
        @SerializedName("minimum_amount")
        val minimumAmount: Int = 0,
        @SerializedName("maximum_amount")
        val maximumAmount: Int = 0,
        @SerializedName("fee")
        val fee: Int = 0,
        @SerializedName("wallet_amount")
        val walletAmount: Int = 0
)

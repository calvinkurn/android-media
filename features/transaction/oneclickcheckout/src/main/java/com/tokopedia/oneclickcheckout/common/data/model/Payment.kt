package com.tokopedia.oneclickcheckout.common.data.model

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
        val minimumAmount: Long = 0,
        @SerializedName("maximum_amount")
        val maximumAmount: Long = 0,
        @SerializedName("fee")
        val fee: Long = 0,
        @SerializedName("wallet_amount")
        val walletAmount: Long = 0,
        @SerializedName("metadata")
        val metadata: String = ""
)

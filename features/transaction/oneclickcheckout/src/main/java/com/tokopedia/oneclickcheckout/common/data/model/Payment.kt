package com.tokopedia.oneclickcheckout.common.data.model

import com.google.gson.annotations.SerializedName

data class Payment(
        @SerializedName("enable")
        val enable: Int = 0,
        @SerializedName("active")
        val active: Int = 0,
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
        val metadata: String = "",
        @SerializedName("mdr")
        val mdr: Float = 0f
)

data class PaymentCreditCard(
        @SerializedName("total_cards")
        val totalCards: Int = 0,
        @SerializedName("bank_code")
        val bankCode: String = "",
        @SerializedName("card_type")
        val cardType: String = "",
        @SerializedName("is_expired")
        val isExpired: Boolean = false
)

data class InstallmentTerm(
        @SerializedName("term")
        val term: Int = 0,
        @SerializedName("mdr")
        val mdr: Float = 0f,
        @SerializedName("mdr_subsidize")
        val mdrSubsidize: Float = 0f,
        @SerializedName("min_amount")
        val minAmount: Long = 0,
        @SerializedName("is_selected")
        val isSelected: Boolean = false
)

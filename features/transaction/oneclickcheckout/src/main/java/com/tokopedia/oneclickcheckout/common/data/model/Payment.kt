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
        val fee: Double = 0.0,
        @SerializedName("wallet_amount")
        val walletAmount: Long = 0,
        @SerializedName("metadata")
        val metadata: String = "",
        @SerializedName("mdr")
        val mdr: Float = 0f,
        @SerializedName("credit_card")
        val creditCard: PaymentCreditCard = PaymentCreditCard(),
        @SerializedName("error_message")
        val errorMessage: PaymentErrorMessage = PaymentErrorMessage()
)

data class PaymentErrorMessage(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("button")
        val button: PaymentErrorMessageButton = PaymentErrorMessageButton()
)

data class PaymentErrorMessageButton(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = ""
)

data class PaymentCreditCard(
        @SerializedName("number_of_cards")
        val numberOfCards: PaymentCreditCardsNumber = PaymentCreditCardsNumber(),
        @SerializedName("available_terms")
        val availableTerms: List<InstallmentTerm> = emptyList(),
        @SerializedName("bank_code")
        val bankCode: String = "",
        @SerializedName("card_type")
        val cardType: String = "",
        @SerializedName("is_expired")
        val isExpired: Boolean = false,
        @SerializedName("tnc_info")
        val tncInfo: String = ""
)

data class PaymentCreditCardsNumber(
        @SerializedName("available")
        val availableCards: Int = 0,
        @SerializedName("unavailable")
        val unavailableCards: Int = 0,
        @SerializedName("total")
        val totalCards: Int = 0
)

data class InstallmentTerm(
        @SerializedName("term")
        val term: Int = 0,
        @SerializedName("mdr")
        val mdr: Float = 0f,
        @SerializedName("mdr_subsidized")
        val mdrSubsidize: Float = 0f,
        @SerializedName("min_amount")
        val minAmount: Long = 0,
        @SerializedName("is_selected")
        val isSelected: Boolean = false
)

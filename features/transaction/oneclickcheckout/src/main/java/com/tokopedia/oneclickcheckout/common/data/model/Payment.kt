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
        val errorMessage: PaymentErrorMessage = PaymentErrorMessage(),
        @SerializedName("occ_revamp_error_message")
        val occRevampErrorMessage: PaymentRevampErrorMessage = PaymentRevampErrorMessage(),
        @SerializedName("ticker_message")
        val tickerMessage: String = "",
        @SerializedName("is_enable_next_button")
        val isEnableNextButton: Boolean = false,
        @SerializedName("is_disable_pay_button")
        val isDisablePayButton: Boolean = false,
        @SerializedName("is_ovo_only_campaign")
        val isOvoOnlyCampaign: Boolean = false,
        @SerializedName("ovo_additional_data")
        val ovoAdditionalData: OvoAdditionalData= OvoAdditionalData()
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

data class PaymentRevampErrorMessage(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("button")
        val button: PaymentRevampErrorMessageButton = PaymentRevampErrorMessageButton()
)

data class PaymentRevampErrorMessageButton(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("action")
        val action: String = ""
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

data class OvoAdditionalData(
        @SerializedName("ovo_activation")
        val ovoActivationData: OvoActionData = OvoActionData(),
        @SerializedName("ovo_top_up")
        val ovoTopUpData: OvoActionData = OvoActionData(),
        @SerializedName("phone_number_registered")
        val phoneNumberRegistered: OvoActionData = OvoActionData()
)

data class OvoActionData(
        @SerializedName("is_required")
        val isRequired: Boolean = false,
        @SerializedName("button_title")
        val buttonTitle: String = "",
        @SerializedName("error_message")
        val errorMessage: String = "",
        @SerializedName("error_ticker")
        val errorTicker: String = "",
        @SerializedName("is_hide_digital")
        val isHideDigital: Int = 0
)

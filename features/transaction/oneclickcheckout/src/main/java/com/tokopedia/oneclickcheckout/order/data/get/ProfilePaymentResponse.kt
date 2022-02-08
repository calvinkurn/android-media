package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class Payment(
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
        @SerializedName("minimum_amount")
        val minimumAmount: Long = 0,
        @SerializedName("maximum_amount")
        val maximumAmount: Long = 0,
        @SerializedName("wallet_amount")
        val walletAmount: Long = 0,
        @SerializedName("fee")
        val fee: Double = 0.0,
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
        @SerializedName("is_disable_pay_button")
        val isDisablePayButton: Boolean = false,
        @SerializedName("is_ovo_only_campaign")
        val isOvoOnlyCampaign: Boolean = false,
        @SerializedName("ovo_additional_data")
        val ovoAdditionalData: OvoAdditionalData = OvoAdditionalData(),
        @SerializedName("bid")
        val bid: String = "",
        @SerializedName("specific_gateway_campaign_only_type")
        val specificGatewayCampaignOnlyType: Int = 0,
        @SerializedName("wallet_additional_data")
        val walletAdditionalData: WalletAdditionalData = WalletAdditionalData()
)

class PaymentErrorMessage(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("button")
        val button: PaymentErrorMessageButton = PaymentErrorMessageButton()
)

class PaymentErrorMessageButton(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = ""
)

class PaymentRevampErrorMessage(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("button")
        val button: PaymentRevampErrorMessageButton = PaymentRevampErrorMessageButton()
)

class PaymentRevampErrorMessageButton(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("action")
        val action: String = ""
)

class PaymentCreditCard(
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
        val tncInfo: String = "",
        @SerializedName("is_afpb")
        val isAfpb: Boolean = false,
        @SerializedName("unix_timestamp")
        val unixTimestamp: String = "",
        @SerializedName("token_id")
        val tokenId: String = "",
        @SerializedName("tenor_signature")
        val tenorSignature: String = ""
)

class PaymentCreditCardsNumber(
        @SerializedName("available")
        val availableCards: Int = 0,
        @SerializedName("unavailable")
        val unavailableCards: Int = 0,
        @SerializedName("total")
        val totalCards: Int = 0
)

class InstallmentTerm(
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

class OvoAdditionalData(
        @SerializedName("ovo_activation")
        val ovoActivationData: OvoActionData = OvoActionData(),
        @SerializedName("ovo_top_up")
        val ovoTopUpData: OvoActionData = OvoActionData(),
        @SerializedName("phone_number_registered")
        val phoneNumberRegistered: OvoActionData = OvoActionData()
)

class WalletAdditionalData(
        @SerializedName("wallet_type")
        val walletType: Int = 0, // 1 for ovo, 2 for gopay, 3 for gopaylater
        @SerializedName("enable_wallet_amount_validation")
        val enableWalletAmountValidation: Boolean = false,
        @SerializedName("activation")
        val activation: WalletData = WalletData(),
        @SerializedName("top_up")
        val topUp: WalletData = WalletData(),
        @SerializedName("phone_number_registered")
        val phoneNumberRegistered: WalletData = WalletData(),
        @SerializedName("gocicil")
        val goCicilData: GoCicilData = GoCicilData(),
        @SerializedName("error_toaster")
        val errorToaster: String = "",
)

class OvoActionData(
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

class WalletData(
        @SerializedName("is_required")
        val isRequired: Boolean = false, // flag to client to show activation button title or activation?
        @SerializedName("button_title")
        val buttonTitle: String = "",
        @SerializedName("success_toaster")
        val successToaster: String = "",
        @SerializedName("error_toaster")
        val errorToaster: String = "",
        @SerializedName("error_message")
        val errorMessage: String = "",
        @SerializedName("is_hide_digital")
        val isHideDigital: Boolean = false,
        @SerializedName("header_title")
        val headerTitle: String = "",
        @SerializedName("url_link")
        val urlLink: String = ""
)

class GoCicilData(
        @SerializedName("error_message_invalid_tenure")
        val errorMessageInvalidTenure: String = "",
        @SerializedName("error_message_top_limit")
        val errorMessageTopLimit: String = "",
        @SerializedName("error_message_bottom_limit")
        val errorMessageBottomLimit: String = "",
)

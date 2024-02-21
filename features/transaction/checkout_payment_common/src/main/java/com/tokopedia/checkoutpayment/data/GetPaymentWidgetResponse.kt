package com.tokopedia.checkoutpayment.data

import com.google.gson.annotations.SerializedName

data class GetPaymentWidgetResponse(
    @SerializedName("payment_data")
    val paymentWidgetData: List<PaymentWidgetDataResponse> = emptyList(),
    @SerializedName("payment_fee_detail")
    val paymentFeeDetails: List<PaymentFeeDetailResponse> = emptyList()
)

data class PaymentWidgetDataResponse(
    @SerializedName("gateway_code")
    val gatewayCode: String = "",
    @SerializedName("gateway_name")
    val gatewayName: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("amount_validation")
    val amountValidation: PaymentAmountValidationResponse = PaymentAmountValidationResponse(),
    @SerializedName("profile_code")
    val profileCode: String = "",
    @SerializedName("merchant_code")
    val merchantCode: String = "",
    @SerializedName("unix_timestamp")
    val unixTimestamp: String = "",
    @SerializedName("bid")
    val bid: String = "",
    @SerializedName("callback_url")
    val callbackUrl: String = "",
    @SerializedName("installment_payment_data")
    val installmentPaymentData: PaymentInstallmentDataResponse = PaymentInstallmentDataResponse(),
    @SerializedName("wallet_data")
    val walletData: PaymentWalletDataResponse = PaymentWalletDataResponse(),
    @SerializedName("ticker_message")
    val tickerMessage: String = "",
    @SerializedName("error_details")
    val errorDetails: PaymentErrorDetailResponse = PaymentErrorDetailResponse(),
    @SerializedName("mandatory_hit")
    val mandatoryHit: List<String> = emptyList(),
    @SerializedName("metadata")
    val metadata: String = ""
)

data class PaymentAmountValidationResponse(
    @SerializedName("minimum_amount")
    val minimumAmount: Long = 0,
    @SerializedName("maximum_amount")
    val maximumAmount: Long = 0,
    @SerializedName("minimum_amount_error_message")
    val minimumAmountErrorMessage: String = "",
    @SerializedName("maximum_amount_error_message")
    val maximumAmountErrorMessage: String = ""
)

data class PaymentInstallmentDataResponse(
    @SerializedName("selected_tenure")
    val selectedTenure: Int = 0,
    @SerializedName("credit_card_attribute")
    val creditCardAttribute: PaymentCreditCardDataResponse = PaymentCreditCardDataResponse(),
    @SerializedName("error_message_invalid_tenure")
    val errorMessageInvalidTenure: String = "",
    @SerializedName("error_message_unavailable_tenure")
    val errorMessageUnavailableTenure: String = "",
    @SerializedName("error_message_top_limit")
    val errorMessageTopLimit: String = "",
    @SerializedName("error_message_bottom_limit")
    val errorMessageBottomLimit: String = ""
)

data class PaymentCreditCardDataResponse(
    @SerializedName("bank_code")
    val bankCode: String = "",
    @SerializedName("card_type")
    val cardType: String = "",
    @SerializedName("tnc_info")
    val tncInfo: String = "",
    @SerializedName("token_id")
    val tokenId: String = "",
    @SerializedName("tenure_signature")
    val tenureSignature: String = "",
    @SerializedName("masked_number")
    val maskedNumber: String = ""
)

data class PaymentWalletDataResponse(
    @SerializedName("wallet_type")
    val walletType: Int = 0,
    @SerializedName("wallet_amount")
    val walletAmount: Long = 0,
    @SerializedName("phone_number_registered")
    val phoneNumberRegistered: String = "",
    @SerializedName("activation")
    val activation: PaymentWalletActionResponse = PaymentWalletActionResponse(),
    @SerializedName("top_up")
    val topUp: PaymentWalletActionResponse = PaymentWalletActionResponse(),
    @SerializedName("phone_number_registration")
    val phoneNumberRegistration: PaymentWalletActionResponse = PaymentWalletActionResponse(),
)

data class PaymentWalletActionResponse(
    @SerializedName("is_required")
    val isRequired: Boolean = false, // flag to client to show activation button title or activation?
    @SerializedName("header_title")
    val headerTitle: String = "",
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
    @SerializedName("url_link")
    val urlLink: String = ""
)

data class PaymentErrorDetailResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("button")
    val button: PaymentErrorDetailButtonResponse = PaymentErrorDetailButtonResponse()
)

class PaymentErrorDetailButtonResponse(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("action")
    val action: String = ""
)

data class PaymentFeeDetailResponse(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("amount")
    val amount: Double = 0.0,
    @SerializedName("type")
    val type: Int = 0,
    @SerializedName("show_tooltip")
    val showTooltip: Boolean = false,
    @SerializedName("tooltip_info")
    val tooltipInfo: String = "",
    @SerializedName("show_slashed")
    val showSlashed: Boolean = false,
    @SerializedName("slashed_fee")
    val slashedFee: Int = 0
)

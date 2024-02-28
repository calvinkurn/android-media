package com.tokopedia.checkoutpayment.domain

data class PaymentWidgetListData(
    val paymentWidgetData: List<PaymentWidgetData> = emptyList(),
    val paymentFeeDetails: List<PaymentFeeDetail> = emptyList()
)

data class PaymentWidgetData(
    val gatewayCode: String = "",
    val gatewayName: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val amountValidation: PaymentAmountValidation = PaymentAmountValidation(),
    val profileCode: String = "",
    val merchantCode: String = "",
    val unixTimestamp: String = "",
    val bid: String = "",
    val callbackUrl: String = "",
    val installmentPaymentData: PaymentInstallmentData = PaymentInstallmentData(),
    val walletData: PaymentWalletData = PaymentWalletData(),
    val tickerMessage: String = "",
    val errorDetails: PaymentErrorDetail = PaymentErrorDetail(),
    val mandatoryHit: List<String> = emptyList(),
    val metadata: String = ""
)

data class PaymentAmountValidation(
    val minimumAmount: Long = 0,
    val maximumAmount: Long = 0,
    val minimumAmountErrorMessage: String = "",
    val maximumAmountErrorMessage: String = ""
)

data class PaymentInstallmentData(
    val selectedTenure: Int = 0,
    val creditCardAttribute: PaymentCreditCardData = PaymentCreditCardData(),
    val errorMessageInvalidTenure: String = "",
    val errorMessageUnavailableTenure: String = "",
    val errorMessageTopLimit: String = "",
    val errorMessageBottomLimit: String = ""
)

data class PaymentCreditCardData(
    val bankCode: String = "",
    val cardType: String = "",
    val tncInfo: String = "",
    val tokenId: String = "",
    val tenureSignature: String = "",
    val maskedNumber: String = ""
)

data class PaymentWalletData(
    val walletType: Int = 0,
    val walletAmount: Long = 0,
    val phoneNumberRegistered: String = "",
    val activation: PaymentWalletAction = PaymentWalletAction(),
    val topUp: PaymentWalletAction = PaymentWalletAction(),
    val phoneNumberRegistration: PaymentWalletAction = PaymentWalletAction(),
)

data class PaymentWalletAction(
    val isRequired: Boolean = false, // flag to client to show activation button title or activation?
    val headerTitle: String = "",
    val buttonTitle: String = "",
    val successToaster: String = "",
    val errorToaster: String = "",
    val errorMessage: String = "",
    val isHideDigital: Boolean = false,
    val urlLink: String = ""
)

data class PaymentErrorDetail(
    val message: String = "",
    val button: PaymentErrorDetailButton = PaymentErrorDetailButton()
)

data class PaymentErrorDetailButton(
    val text: String = "",
    val action: String = ""
)

data class PaymentFeeDetail(
    val title: String = "",
    val amount: Double = 0.0,
    val type: Int = 0,
    val showTooltip: Boolean = false,
    val tooltipInfo: String = "",
    val showSlashed: Boolean = false,
    val slashedFee: Int = 0
)

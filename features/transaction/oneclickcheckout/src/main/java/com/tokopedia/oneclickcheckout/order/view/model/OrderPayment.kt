package com.tokopedia.oneclickcheckout.order.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class OrderPayment(
        val isEnable: Boolean = false,
        val isCalculationError: Boolean = false,
        val gatewayCode: String = "",
        val gatewayName: String = "",
        val minimumAmount: Long = 0,
        val maximumAmount: Long = 0,
        val fee: Double = 0.0,
        val walletAmount: Long = 0,
        val creditCard: OrderPaymentCreditCard = OrderPaymentCreditCard(),
        val errorMessage: OrderPaymentErrorMessage = OrderPaymentErrorMessage(),
        val revampErrorMessage: OrderPaymentRevampErrorMessage = OrderPaymentRevampErrorMessage(),
        val isDisablePayButton: Boolean = false,
        // flag to determine continue using ovo flow
        val isOvoOnlyCampaign: Boolean = false,
        val ovoData: OrderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(),
        val walletErrorData: OrderPaymentWalletErrorData? = null,
        val errorData: OrderPaymentErrorData? = null,
        val bid: String = "",
        val specificGatewayCampaignOnlyType: Int = 0,
        val walletData: OrderPaymentWalletAdditionalData = OrderPaymentWalletAdditionalData(),
        val originalPaymentFees: List<OrderPaymentFee> = emptyList(),
        val dynamicPaymentFees: List<OrderPaymentFee>? = emptyList()
) {
    val isOvo: Boolean
        get() = gatewayCode.contains("OVO")

    fun isError(): Boolean {
        return isCalculationError || errorData != null || walletErrorData != null
    }

    fun getRealFee(): Double {
        if (creditCard.selectedTerm != null) {
            return creditCard.selectedTerm.fee
        }
        if (walletData.isGoCicil) {
            return walletData.goCicilData.selectedTerm?.feeAmount ?: 0.0
        }
        return getTotalPaymentFee()
    }

    fun isInstallment(): Boolean {
        return creditCard.selectedTerm != null || walletData.isGoCicil
    }

    fun getTotalPaymentFee(): Double {
        return originalPaymentFees.sumOf { it.fee } + (dynamicPaymentFees?.sumOf { it.fee } ?: 0.0)
    }
}

data class OrderPaymentErrorMessage(
        val message: String = "",
        val button: OrderPaymentErrorMessageButton = OrderPaymentErrorMessageButton()
)

data class OrderPaymentErrorMessageButton(
        val text: String = "",
        val link: String = ""
)

data class OrderPaymentRevampErrorMessage(
        val message: String = "",
        val button: OrderPaymentRevampErrorMessageButton = OrderPaymentRevampErrorMessageButton()
)

data class OrderPaymentRevampErrorMessageButton(
        val text: String = "",
        val action: String = ""
)

data class OrderPaymentErrorData(
        val message: String = "",
        val buttonText: String = "",
        val action: String = ""
) {
    companion object {
        internal const val ACTION_CHANGE_CC = "change_cc"
        internal const val ACTION_CHANGE_PAYMENT = "change_payment"
    }
}

data class OrderPaymentCreditCard(
        val numberOfCards: OrderPaymentCreditCardsNumber = OrderPaymentCreditCardsNumber(),
        val availableTerms: List<OrderPaymentInstallmentTerm> = emptyList(),
        val bankCode: String = "",
        val cardType: String = "",
        val isExpired: Boolean = false,
        val tncInfo: String = "",
        val selectedTerm: OrderPaymentInstallmentTerm? = null,
        val additionalData: OrderPaymentCreditCardAdditionalData = OrderPaymentCreditCardAdditionalData(),
        val isDebit: Boolean = false,
        val isAfpb: Boolean = false,
        val unixTimestamp: String = "",
        val tokenId: String = "",
        val tenorSignature: String = ""
) {
    companion object {
        internal const val DEBIT_GATEWAY_CODE = "DEBITONLINE"
    }
}

data class OrderPaymentCreditCardsNumber(
        val availableCards: Int = 0,
        val unavailableCards: Int = 0,
        val totalCards: Int = 0
)

@Parcelize
data class OrderPaymentCreditCardAdditionalData(
        val id: Long = 0,
        val name: String = "",
        val email: String = "",
        val msisdn: String = "",
        val merchantCode: String = "",
        val profileCode: String = "",
        val signature: String = "",
        val changeCcLink: String = "",
        val callbackUrl: String = "",
        val totalProductPrice: String = ""
) : Parcelable

data class OrderPaymentInstallmentTerm(
        val term: Int = 0,
        val mdr: Float = 0f,
        val mdrSubsidize: Float = 0f,
        val minAmount: Long = 0,
        var isSelected: Boolean = false,
        var isEnable: Boolean = false,
        var isError: Boolean = false,
        var fee: Double = 0.0,
        var monthlyAmount: Double = 0.0,
        var description: String = "",
)

data class OrderPaymentOvoAdditionalData(
        val activation: OrderPaymentOvoActionData = OrderPaymentOvoActionData(),
        val topUp: OrderPaymentOvoActionData = OrderPaymentOvoActionData(),
        val phoneNumber: OrderPaymentOvoActionData = OrderPaymentOvoActionData(),
        val callbackUrl: String = "",
        val customerData: OrderPaymentOvoCustomerData = OrderPaymentOvoCustomerData()
) {
    val isActivationRequired: Boolean
        get() = activation.isRequired

    val isPhoneNumberMissing: Boolean
        get() = phoneNumber.isRequired
}

data class OrderPaymentWalletAdditionalData(
        val walletType: Int = 0,
        val enableWalletAmountValidation: Boolean = false,
        val callbackUrl: String = "",
        val activation: OrderPaymentWalletActionData = OrderPaymentWalletActionData(),
        val topUp: OrderPaymentWalletActionData = OrderPaymentWalletActionData(),
        val phoneNumber: OrderPaymentWalletActionData = OrderPaymentWalletActionData(),
        val goCicilData: OrderPaymentGoCicilData = OrderPaymentGoCicilData(),
) {
    val isActivationRequired: Boolean
        get() = activation.isRequired

    val isPhoneNumberMissing: Boolean
        get() = phoneNumber.isRequired

    val isGoCicil: Boolean
        get() = walletType == WALLET_TYPE_GOPAYLATERCICIL

    companion object {
        const val WALLET_TYPE_OVO = 1
        const val WALLET_TYPE_GOPAY = 2
        const val WALLET_TYPE_GOPAYLATER = 3
        const val WALLET_TYPE_GOPAYLATERCICIL = 4
    }
}

@Parcelize
data class OrderPaymentOvoCustomerData(
        val name: String = "",
        val email: String = "",
        val msisdn: String = ""
) : Parcelable

data class OrderPaymentOvoActionData(
        val isRequired: Boolean = false,
        val buttonTitle: String = "",
        val errorMessage: String = "",
        val errorTicker: String = "",
        val isHideDigital: Int = 0
)

data class OrderPaymentWalletActionData(
        val isRequired: Boolean = false,
        val buttonTitle: String = "",
        val successToaster: String = "",
        val errorToaster: String = "",
        val errorMessage: String = "",
        val isHideDigital: Boolean = false,
        val headerTitle: String = "",
        val urlLink: String = ""
)

data class OrderPaymentWalletErrorData(
        val isBlockingError: Boolean = false,
        val message: String = "",
        val buttonTitle: String = "",
        val type: Int = 0,
        val callbackUrl: String = "",
        val isHideDigital: Int = 0,
        val isOvo: Boolean = false
) {
    companion object {
        const val TYPE_ACTIVATION = 1
        const val TYPE_TOP_UP = 2
        const val TYPE_MISSING_PHONE = 3
    }
}

data class OrderPaymentGoCicilData(
        val selectedTerm: OrderPaymentGoCicilTerms? = null,
        val availableTerms: List<OrderPaymentGoCicilTerms> = emptyList(),
        val errorMessageInvalidTenure: String = "",
        val errorMessageTopLimit: String = "",
        val errorMessageBottomLimit: String = "",
        val errorMessageUnavailableTenures: String = "",
        val selectedTenure: Int = 0,
) {
    val hasValidTerm: Boolean
        get() = selectedTerm != null && selectedTerm.isActive && availableTerms.isNotEmpty()

    val hasInvalidTerm: Boolean
        get() = selectedTerm != null && !selectedTerm.isActive && availableTerms.isNotEmpty()
}

data class OrderPaymentGoCicilTerms(
        val installmentTerm: Int = 0,
        val optionId: String = "",
        val firstInstallmentDate: String = "",
        val lastInstallmentDate: String = "",
        val firstDueMessage: String = "",
        val interestAmount: Double = 0.0,
        val feeAmount: Double = 0.0,
        val installmentAmountPerPeriod: Double = 0.0,
        val labelType: String = "",
        val labelMessage: String = "",
        val isActive: Boolean = false,
        val description: String = "",
        val isRecommended: Boolean = false,
) {
    val hasPromoLabel: Boolean
        get() = labelMessage.isNotBlank()
}

data class OrderPaymentFee(
    val title: String = "",
    val fee: Double = 0.0,
    val showTooltip: Boolean = false,
    val showSlashed: Boolean = false,
    val slashedFee: Int = 0,
    val tooltipInfo: String = "",
)

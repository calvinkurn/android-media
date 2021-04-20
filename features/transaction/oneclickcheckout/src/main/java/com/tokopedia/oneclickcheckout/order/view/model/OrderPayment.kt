package com.tokopedia.oneclickcheckout.order.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
        val errorTickerMessage: String = "",
        val isEnableNextButton: Boolean = false,
        val isDisablePayButton: Boolean = false,
        // flag to determine continue using ovo flow
        val isOvoOnlyCampaign: Boolean = false,
        val ovoData: OrderPaymentOvoAdditionalData = OrderPaymentOvoAdditionalData(),
        val ovoErrorData: OrderPaymentOvoErrorData? = null,
        val errorData: OrderPaymentErrorData? = null
) {
    val isOvo: Boolean
        get() = gatewayCode.contains("OVO")

    fun isError(): Boolean {
        return isCalculationError || errorMessage.message.isNotEmpty() || errorTickerMessage.isNotEmpty()
    }

    fun getRealFee(): Double {
        return creditCard.selectedTerm?.fee ?: fee
    }

    fun hasCreditCardOption(): Boolean {
        if (creditCard.numberOfCards.totalCards > 0 && creditCard.numberOfCards.availableCards > 0) return true
        return false
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
        val isDebit: Boolean = false
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
        val callbackUrl: String = ""
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
        var monthlyAmount: Double = 0.0
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

@Parcelize
data class OrderPaymentOvoCustomerData(
        val name: String = "",
        val email: String = "",
        val msisdn: String = ""
): Parcelable

data class OrderPaymentOvoActionData(
        val isRequired: Boolean = false,
        val buttonTitle: String = "",
        val errorMessage: String = "",
        val errorTicker: String = "",
        val isHideDigital: Int = 0
)

data class OrderPaymentOvoErrorData(
        val isBlockingError: Boolean = false,
        val message: String = "",
        val buttonTitle: String = "",
        val type: Int = 0,
        val callbackUrl: String = "",
        val isHideDigital: Int = 0
) {
    companion object {
        const val TYPE_ACTIVATION = 1
        const val TYPE_TOP_UP = 2
        const val TYPE_MISSING_PHONE = 3
    }
}
package com.tokopedia.oneclickcheckout.order.view.model

data class OrderPayment(
        val isEnable: Boolean = false,
        val isCalculationError: Boolean = false,
        val gatewayCode: String = "",
        val gatewayName: String = "",
        val image: String = "",
        val description: String = "",
        val minimumAmount: Long = 0,
        val maximumAmount: Long = 0,
        val fee: Double = 0.0,
        val walletAmount: Long = 0,
        val metadata: String = "",
        val creditCard: OrderPaymentCreditCard = OrderPaymentCreditCard(),
        val errorMessage: OrderPaymentErrorMessage = OrderPaymentErrorMessage()
) {
    fun isError(): Boolean {
        return isCalculationError || errorMessage.message.isNotEmpty()
    }

    fun getRealFee(): Double {
        return creditCard.selectedTerm?.fee ?: fee
    }

    fun hasBlockingError(): Boolean {
        return errorMessage.message.isNotEmpty()
    }

    fun hasNoCreditCardOption(): Boolean {
        if (creditCard.numberOfCards.totalCards > 0 && creditCard.numberOfCards.availableCards < 1) return true
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

data class OrderPaymentCreditCard(
        val numberOfCards: OrderPaymentCreditCardsNumber = OrderPaymentCreditCardsNumber(),
        val availableTerms: List<OrderPaymentInstallmentTerm> = emptyList(),
        val bankCode: String = "",
        val cardType: String = "",
        val isExpired: Boolean = false,
        val tncInfo: String = "",
        val selectedTerm: OrderPaymentInstallmentTerm? = null
)

data class OrderPaymentCreditCardsNumber(
        val availableCards: Int = 0,
        val unavailableCards: Int = 0,
        val totalCards: Int = 0
)

data class OrderPaymentInstallmentTerm(
        val term: Int = 0,
        val mdr: Float = 0f,
        val mdrSubsidize: Float = 0f,
        val minAmount: Long = 0,
        var isSelected: Boolean = false,
        var isEnable: Boolean = false,
        var fee: Double = 0.0,
        var monthlyAmount: Double = 0.0
)
package com.tokopedia.oneclickcheckout.order.view.model

data class OrderPayment(
        val isEnable: Boolean = false,
        val isError: Boolean = false,
        val gatewayCode: String = "",
        val gatewayName: String = "",
        val image: String = "",
        val description: String = "",
        val minimumAmount: Long = 0,
        val maximumAmount: Long = 0,
        val fee: Double = 0.0,
        val walletAmount: Long = 0,
        val metadata: String = "",
        val creditCard: OrderPaymentCreditCard? = null,
        val errorMessage: OrderPaymentErrorMessage? = null
) {
    fun getRealFee(): Double {
        return creditCard?.selectedTerm?.fee ?: fee
    }
}
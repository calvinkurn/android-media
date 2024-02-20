package com.tokopedia.checkoutpayment.domain

data class GoCicilInstallmentData(
    val tickerMessage: String = "",
    val installmentOptions: List<GoCicilInstallmentOption> = emptyList()
)

data class GoCicilInstallmentOption(
    val installmentTerm: Int = 0,
    val optionId: String = "",
    val firstInstallmentTime: String = "",
    val estInstallmentEnd: String = "",
    val firstDueMessage: String = "",
    val interestAmount: Double = 0.0,
    val feeAmount: Double = 0.0,
    val installmentAmountPerPeriod: Double = 0.0,
    val labelType: String = "",
    val labelMessage: String = "",
    val isActive: Boolean = false,
    val description: String = "",
    val isRecommended: Boolean = false
)

package com.tokopedia.oneclickcheckout.order.view.model

data class OrderCost(
        val totalPrice: Double = 0.0,
        val totalItemPrice: Double = 0.0,
        val shippingFee: Double = 0.0,
        val insuranceFee: Double = 0.0,
        val isUseInsurance: Boolean = false,
        val paymentFee: Double = 0.0,
        val shippingDiscountAmount: Int = 0,
        val productDiscountAmount: Int = 0,
        val purchaseProtectionPrice: Int = 0,
        val cashbacks: List<OrderCostCashbackData> = emptyList(),
        val installmentData: OrderCostInstallmentData? = null,

        // Easy access for calculation
        val totalPriceWithoutPaymentFees: Double = 0.0,
        val totalPriceWithoutDiscountsAndPaymentFees: Double = 0.0,
        val totalItemPriceAndShippingFee: Double = 0.0,
        val totalAdditionalFee: Double = 0.0,
        val totalDiscounts: Int = 0,

        // Flag
        val isNewBottomSheet: Boolean = false,
)

data class OrderCostCashbackData(
        val description: String = "",
        val amountStr: String = "",
        val currencyDetailStr: String = "",
)

data class OrderCostInstallmentData(
        val installmentFee: Double = 0.0,
        val installmentTerm: Int = 0,
        val installmentAmountPerPeriod: Double = 0.0,
        val installmentFirstDate: String = "",
        val installmentLastDate: String = "",
)
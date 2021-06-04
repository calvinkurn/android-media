package com.tokopedia.minicart.cartlist

data class MiniCartSummaryTransactionUiModel(
        var qty: Int = 0,
        var totalWording: String = "",
        var totalValue: Long = 0,
        var discountTotalWording: String = "",
        var discountValue: Long = 0,
        var paymentTotalWording: String = "",
        var paymentTotal: Long = 0,
)
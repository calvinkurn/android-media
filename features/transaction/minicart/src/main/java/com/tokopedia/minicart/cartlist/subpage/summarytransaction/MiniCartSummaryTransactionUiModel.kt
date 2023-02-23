package com.tokopedia.minicart.cartlist.subpage.summarytransaction

data class MiniCartSummaryTransactionUiModel(
        var qty: Int = 0,
        var totalWording: String = "",
        var totalValue: Double = 0.0,
        var discountTotalWording: String = "",
        var discountValue: Double = 0.0,
        var paymentTotalWording: String = "",
        var paymentTotal: Double = 0.0,
        var sellerCashbackWording: String = "",
        var sellerCashbackValue: Double = 0.0
)

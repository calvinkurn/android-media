package com.tokopedia.cart.domain.model.cartlist

data class SummaryTransactionUiModel(
        var qty: String = "",
        var totalWording: String = "",
        var totalValue: Long = 0,
        var discountTotalWording: String = "",
        var discountValue: Long = 0,
        var paymentTotalWording: String = "",
        var paymentTotal: Long = 0,
        var promoWording: String = "",
        var promoValue: Long = 0,
        var sellerCashbackWording: String = "",
        var sellerCashbackValue: Long = 0
)
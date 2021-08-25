package com.tokopedia.cart.domain.model.cartlist

data class SummaryTransactionUiModel(
        var qty: String = "",
        var totalWording: String = "",
        var totalValue: Int = 0,
        var discountTotalWording: String = "",
        var discountValue: Int = 0,
        var paymentTotalWording: String = "",
        var paymentTotal: Int = 0,
        var promoWording: String = "",
        var promoValue: Int = 0,
        var sellerCashbackWording: String = "",
        var sellerCashbackValue: Int = 0
)
package com.tokopedia.cartrevamp.domain.model.cartlist

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
    var sellerCashbackValue: Long = 0,
    var listSummaryAddOns: List<SummaryAddOns> = emptyList()
) {
    data class SummaryAddOns(
        var wording: String = "",
        var type: Int = -1,
        var qty: Int = -1,
        var priceLabel: String = "",
        var priceValue: Double = 0.0
    )
}

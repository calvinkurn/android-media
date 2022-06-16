package com.tokopedia.sellerorder.detail.presentation.model

data class AddOnSummaryUiModel(
    val addons: List<AddOnUiModel>,
    val total: Int,
    val totalPrice: String,
    val totalPriceStr: String,
    val totalQuantity: Int,
    val iconUrl: String,
    val label: String
)
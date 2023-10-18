package com.tokopedia.buyerorderdetail.presentation.model

data class PofRefundSummaryUiModel(
    val totalAmountLabel: String,
    val totalAmountValue: String,
    val footerInfo: String,
    val detailsSummary: List<PofSummaryInfoUiModel>
)

data class PofSummaryInfoUiModel(
    val label: String,
    val value: String
)

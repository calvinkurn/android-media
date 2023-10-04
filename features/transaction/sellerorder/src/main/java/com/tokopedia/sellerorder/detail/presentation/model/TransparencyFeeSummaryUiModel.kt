package com.tokopedia.sellerorder.detail.presentation.model

data class TransparencyFeeSummaryUiModel(
    val value: String,
    val attributes: List<BaseTransparencyFeeAttributes>,
    val note: String,
    val state: String
)

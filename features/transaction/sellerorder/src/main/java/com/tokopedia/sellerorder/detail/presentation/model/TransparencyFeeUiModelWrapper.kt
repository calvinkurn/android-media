package com.tokopedia.sellerorder.detail.presentation.model

data class TransparencyFeeUiModelWrapper(
    val bottomSheetTitle: String,
    val transparencyFeeList: List<BaseTransparencyFee>,
    val summary: TransparencyFeeSummaryUiModel
)

package com.tokopedia.sellerorder.detail.presentation.model.transparency_fee

data class TransparencyFeeUiModelWrapper(
    val bottomSheetTitle: String,
    val transparencyFeeList: List<BaseTransparencyFee>,
    val summary: TransparencyFeeSummaryUiModel
)

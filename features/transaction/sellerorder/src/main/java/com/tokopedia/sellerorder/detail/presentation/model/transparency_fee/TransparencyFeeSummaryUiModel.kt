package com.tokopedia.sellerorder.detail.presentation.model.transparency_fee

import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.BaseTransparencyFeeAttributes

data class TransparencyFeeSummaryUiModel(
    val value: String,
    val attributes: List<BaseTransparencyFeeAttributes>,
    val note: String,
    val state: String
)

package com.tokopedia.review.common.presentation.uimodel

data class ReviewProductUiModel(
        var productID: Int? = 0,
        var productImageUrl: String? = "",
        var productName: String? = "",
        var variantName: String? = ""
)
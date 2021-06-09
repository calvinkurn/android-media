package com.tokopedia.review.common.presentation.uimodel

data class ReviewProductUiModel(
        var productID: Long? = 0,
        var productImageUrl: String? = "",
        var productName: String? = "",
        var variantName: String? = ""
)
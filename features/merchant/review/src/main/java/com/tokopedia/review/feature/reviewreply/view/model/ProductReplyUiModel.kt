package com.tokopedia.review.feature.reviewreply.view.model

data class ProductReplyUiModel(
        var productID: Long? = 0,
        var productImageUrl: String? = "",
        var productName: String? = "",
        var variantName: String? = ""
)
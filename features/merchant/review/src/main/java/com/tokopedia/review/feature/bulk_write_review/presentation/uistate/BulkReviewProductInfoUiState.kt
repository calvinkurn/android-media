package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

sealed interface BulkReviewProductInfoUiState {
    data class Showing(
        val productID: String,
        val productName: String,
        val productImageUrl: String,
        val productVariantName: String,
        val productPurchaseDate: String
    ) : BulkReviewProductInfoUiState
}

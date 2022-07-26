package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.model.ProductData

sealed interface CreateReviewProductCardUiState {
    object Loading : CreateReviewProductCardUiState
    data class Showing(val productData: ProductData) : CreateReviewProductCardUiState
}
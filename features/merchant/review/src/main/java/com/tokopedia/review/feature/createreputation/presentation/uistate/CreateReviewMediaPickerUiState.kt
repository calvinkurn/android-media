package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel

sealed interface CreateReviewMediaPickerUiState {
    object Loading: CreateReviewMediaPickerUiState
    data class Showing(val mediaItems: List<CreateReviewMediaUiModel>): CreateReviewMediaPickerUiState
}
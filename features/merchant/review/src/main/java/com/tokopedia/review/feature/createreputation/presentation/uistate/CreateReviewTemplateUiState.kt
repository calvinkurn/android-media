package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel

sealed interface CreateReviewTemplateUiState {
    val templates: List<CreateReviewTemplateItemUiModel>
    object Loading : CreateReviewTemplateUiState { override val templates: List<CreateReviewTemplateItemUiModel> = emptyList() }
    data class Showing(override val templates: List<CreateReviewTemplateItemUiModel>) : CreateReviewTemplateUiState
    data class Hidden(override val templates: List<CreateReviewTemplateItemUiModel>) : CreateReviewTemplateUiState
    data class Changing(override val templates: List<CreateReviewTemplateItemUiModel>) : CreateReviewTemplateUiState
}
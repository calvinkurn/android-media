package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate

sealed interface CreateReviewTemplateItemUiState {

    fun areItemsTheSame(other: Any?): Boolean
    fun areContentsTheSame(other: Any?): Boolean

    data class Showing(val data: CreateReviewTemplate) : CreateReviewTemplateItemUiState {
        override fun areItemsTheSame(other: Any?): Boolean {
            return other is Showing && data.text == other.data.text
        }

        override fun areContentsTheSame(other: Any?): Boolean {
            return this == other
        }
    }
}
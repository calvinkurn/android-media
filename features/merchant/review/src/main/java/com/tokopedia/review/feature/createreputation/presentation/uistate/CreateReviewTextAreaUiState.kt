package com.tokopedia.review.feature.createreputation.presentation.uistate

import com.tokopedia.review.feature.createreputation.presentation.uimodel.CreateReviewTextAreaTextUiModel
import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface CreateReviewTextAreaUiState {
    fun areContentTheSame(other: Any?): Boolean

    object Loading : CreateReviewTextAreaUiState {
        override fun areContentTheSame(other: Any?): Boolean {
            return other is Loading
        }
    }

    data class Showing(
        val reviewTextAreaTextUiModel: CreateReviewTextAreaTextUiModel,
        val hint: StringRes,
        val helper: StringRes,
        val hasFocus: Boolean
    ) : CreateReviewTextAreaUiState {
        override fun areContentTheSame(other: Any?): Boolean {
            return other is Showing &&
                    hint == other.hint &&
                    helper == other.helper &&
                    hasFocus == other.hasFocus &&
                    other.reviewTextAreaTextUiModel.source == CreateReviewTextAreaTextUiModel.Source.CREATE_REVIEW_TEXT_AREA
        }
    }
}
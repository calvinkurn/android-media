package com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate

import com.tokopedia.review.feature.media.gallery.detailed.presentation.uimodel.DetailedReviewActionMenuUiModel
import java.io.Serializable

sealed interface ActionMenuBottomSheetUiState: Serializable {
    data class Hidden(
        val items: List<DetailedReviewActionMenuUiModel>,
        val feedbackID: String,
        val shopID: String
    ) : ActionMenuBottomSheetUiState

    data class Showing(
        val items: List<DetailedReviewActionMenuUiModel>,
        val feedbackID: String,
        val shopID: String
    ) : ActionMenuBottomSheetUiState
}
package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate

import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uimodel.DetailedReviewActionMenuUiModel
import java.io.Serializable

sealed interface DetailedReviewActionMenuBottomSheetUiState: Serializable {
    data class Hidden(
        val items: List<DetailedReviewActionMenuUiModel>,
        val feedbackID: String,
        val shopID: String
    ) : DetailedReviewActionMenuBottomSheetUiState

    data class Showing(
        val items: List<DetailedReviewActionMenuUiModel>,
        val feedbackID: String,
        val shopID: String
    ) : DetailedReviewActionMenuBottomSheetUiState
}
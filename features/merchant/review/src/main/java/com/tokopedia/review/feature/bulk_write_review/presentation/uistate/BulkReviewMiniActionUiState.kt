package com.tokopedia.review.feature.bulk_write_review.presentation.uistate

import com.tokopedia.reviewcommon.uimodel.StringRes

sealed interface BulkReviewMiniActionUiState {
    val iconUnifyId: Int
    val text: StringRes

    data class Hidden(
        override val iconUnifyId: Int,
        override val text: StringRes
    ) : BulkReviewMiniActionUiState

    data class Showing(
        override val iconUnifyId: Int,
        override val text: StringRes
    ) : BulkReviewMiniActionUiState
}

package com.tokopedia.review.feature.createreputation.presentation.uimodel

import com.tokopedia.reviewcommon.uimodel.StringRes

data class CreateReviewToasterUiModel<T: Any>(
    val message: StringRes,
    val actionText: StringRes,
    val duration: Int,
    val type: Int,
    val id: Int = -1,
    val payload: T
)

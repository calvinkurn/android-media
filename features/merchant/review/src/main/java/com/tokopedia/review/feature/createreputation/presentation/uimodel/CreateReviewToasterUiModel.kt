package com.tokopedia.review.feature.createreputation.presentation.uimodel

import com.tokopedia.reviewcommon.uimodel.StringRes

data class CreateReviewToasterUiModel(
    val message: StringRes,
    val actionText: StringRes,
    val duration: Int,
    val type: Int
)

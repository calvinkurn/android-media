package com.tokopedia.review.feature.inbox.pending.presentation.adapter

import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel

interface ReviewPendingTypeFactory {
    fun type(reviewPendingUiModel: ReviewPendingUiModel): Int
}
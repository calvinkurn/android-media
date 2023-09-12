package com.tokopedia.review.feature.inbox.pending.presentation.adapter

import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.BulkReviewUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityCarouselUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingOvoIncentiveUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel

interface ReviewPendingTypeFactory {
    fun type(reviewPendingUiModel: ReviewPendingUiModel): Int
    fun type(reviewPendingOvoIncentiveUiModel: ReviewPendingOvoIncentiveUiModel): Int
    fun type(reviewPendingEmptyUiModel: ReviewPendingEmptyUiModel): Int
    fun type(reviewPendingCredibilityCarouselUiModel: ReviewPendingCredibilityCarouselUiModel): Int
    fun type(bulkReviewUiModel: BulkReviewUiModel): Int
}

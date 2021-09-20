package com.tokopedia.review.feature.inbox.pending.presentation.adapter

import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingOvoIncentiveUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel

interface ReviewPendingTypeFactory {
    fun type(reviewPendingUiModel: ReviewPendingUiModel): Int
    fun type(reviewPendingOvoIncentiveUiModel: ReviewPendingOvoIncentiveUiModel): Int
    fun type(reviewPendingCredibilityUiModel: ReviewPendingCredibilityUiModel): Int
    fun type(reviewPendingEmptyUiModel: ReviewPendingEmptyUiModel): Int
}
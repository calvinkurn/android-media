package com.tokopedia.review.feature.inbox.history.presentation.adapter

import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel

interface ReviewHistoryTypeFactory {
    fun type(reviewHistoryUiModel: ReviewHistoryUiModel): Int
}
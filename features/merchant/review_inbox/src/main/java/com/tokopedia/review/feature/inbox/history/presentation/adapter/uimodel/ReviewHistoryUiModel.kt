package com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistory
import com.tokopedia.review.feature.inbox.history.presentation.adapter.ReviewHistoryAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

class ReviewHistoryUiModel(
    val productrevFeedbackHistory: ProductrevFeedbackHistory,
    val attachedMediaThumbnail: ReviewMediaThumbnailUiModel
) : Visitable<ReviewHistoryAdapterTypeFactory> {

    override fun type(typeFactory: ReviewHistoryAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
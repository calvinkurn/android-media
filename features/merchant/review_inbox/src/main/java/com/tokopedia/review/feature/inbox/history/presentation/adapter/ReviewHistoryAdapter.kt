package com.tokopedia.review.feature.inbox.history.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable

class ReviewHistoryAdapter(
    reviewHistoryAdapterTypeFactory: ReviewHistoryAdapterTypeFactory
) : BaseListAdapter<ReviewHistoryUiModel, ReviewHistoryAdapterTypeFactory>(reviewHistoryAdapterTypeFactory) {
    fun findReviewHistoryContainingThumbnail(item: ReviewMediaThumbnailVisitable): ReviewHistoryUiModel? {
        return visitables.filterIsInstance<ReviewHistoryUiModel>().find {
            it.attachedMediaThumbnail.mediaThumbnails.contains(item)
        }
    }
}
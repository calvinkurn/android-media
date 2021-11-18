package com.tokopedia.review.feature.inbox.pending.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingOvoIncentiveUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel


class ReviewPendingAdapter(
        reviewPendingAdapterTypeFactory: ReviewPendingAdapterTypeFactory
) : BaseListAdapter<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory>(reviewPendingAdapterTypeFactory) {

    fun insertOvoIncentive(reviewPendingOvoIncentiveUiModel: ReviewPendingOvoIncentiveUiModel) {
        if(visitables.firstOrNull() is ReviewPendingOvoIncentiveUiModel) {
            return
        }
        visitables.add(0, reviewPendingOvoIncentiveUiModel)
        notifyDataSetChanged()
    }

    fun insertCredibilityWidget(reviewPendingCredibilityUiModel: ReviewPendingCredibilityUiModel) {
        if (visitables.filterIsInstance<ReviewPendingCredibilityUiModel>().isEmpty()) {
            visitables.add(reviewPendingCredibilityUiModel)
        }
    }

    fun insertEmptyModel(reviewPendingEmptyUiModel: ReviewPendingEmptyUiModel) {
        if (visitables.filterIsInstance<ReviewPendingEmptyUiModel>().isEmpty()) {
            visitables.add(reviewPendingEmptyUiModel)
        }
    }
}
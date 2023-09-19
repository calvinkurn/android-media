package com.tokopedia.review.feature.inbox.pending.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.BulkReviewUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.CoachMarkUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityCarouselUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingOvoIncentiveUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel

class ReviewPendingAdapter(
    reviewPendingAdapterTypeFactory: ReviewPendingAdapterTypeFactory
) : BaseListAdapter<ReviewPendingUiModel, ReviewPendingAdapterTypeFactory>(reviewPendingAdapterTypeFactory) {

    fun insertOvoIncentive(reviewPendingOvoIncentiveUiModel: ReviewPendingOvoIncentiveUiModel) {
        if (visitables.firstOrNull() is ReviewPendingOvoIncentiveUiModel) {
            return
        }
        visitables.add(0, reviewPendingOvoIncentiveUiModel)
        notifyDataSetChanged()
    }

    fun insertEmptyModel(reviewPendingEmptyUiModel: ReviewPendingEmptyUiModel) {
        if (visitables.filterIsInstance<ReviewPendingEmptyUiModel>().isEmpty()) {
            visitables.add(reviewPendingEmptyUiModel)
        }
    }

    fun insertCredibilityCarouselWidget(credibilityCarouselUiModel: ReviewPendingCredibilityCarouselUiModel) {
        if (visitables.filterIsInstance<ReviewPendingCredibilityCarouselUiModel>().isEmpty()) {
            visitables.add(credibilityCarouselUiModel)
        }
    }

    fun insertBulkReview(bulkReviewUiModel: BulkReviewUiModel) {
        if (visitables.filterIsInstance<BulkReviewUiModel>().isNotEmpty()) return
        val target = visitables.indexOfFirst {
            it is ReviewPendingUiModel
        }.takeIf { it > -1 }
        if (target == null) {
            visitables.add(bulkReviewUiModel)
            notifyItemInserted(visitables.size)
        } else {
            visitables.add(target, bulkReviewUiModel)
            notifyItemInserted(target)
        }
    }

    fun removeBulkReview() {
        visitables.removeAll { it is BulkReviewUiModel }
    }

    fun getItemPosition(uiModel: CoachMarkUiModel?): Int {
        return visitables.indexOfFirst { it == uiModel }
    }

    fun getBaseVisitableUiModels(): List<CoachMarkUiModel> {
        return visitables.filterIsInstance<CoachMarkUiModel>()
    }
}

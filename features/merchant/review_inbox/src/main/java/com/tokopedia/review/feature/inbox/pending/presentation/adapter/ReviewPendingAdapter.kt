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

    /**
     * There is only 1 Bulk Review Recommendation Widget
     * if indexOfBulkReview > -1 : Bulk Review Exists, Update Data
     * else : Bulk Review Not Found, Insert One
     */
    fun insertBulkReview(bulkReviewUiModel: BulkReviewUiModel) {
        val indexOfBulkReview = visitables.indexOfFirst { it is BulkReviewUiModel }
        if (indexOfBulkReview > -1) {
            val currentBulkReview = visitables[indexOfBulkReview] as? BulkReviewUiModel
            currentBulkReview?.data = bulkReviewUiModel.data
            notifyItemChanged(indexOfBulkReview)
        } else {
            val target = visitables.indexOfFirst { it is ReviewPendingUiModel }
            if (target > -1) {
                visitables.add(target, bulkReviewUiModel)
                notifyItemInserted(target)
            } else {
                visitables.add(bulkReviewUiModel)
                notifyItemInserted(visitables.size)
            }
        }
    }

    /**
     * Since Bulk Review Recommendation restricted to be only 1 items.
     * (Restricted at insertBulkReview)
     * It is safe to check using indexOfFirst
     */
    fun removeBulkReview() {
        val target = visitables.indexOfFirst { it is BulkReviewUiModel }
        visitables.removeAt(target)
        notifyItemRemoved(target)
    }

    fun getItemPosition(uiModel: CoachMarkUiModel?): Int {
        return visitables.indexOfFirst { it == uiModel }
    }

    fun getBaseVisitableUiModels(): List<CoachMarkUiModel> {
        return visitables.filterIsInstance<CoachMarkUiModel>()
    }
}

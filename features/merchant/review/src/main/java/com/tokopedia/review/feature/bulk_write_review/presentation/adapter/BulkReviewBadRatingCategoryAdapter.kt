package com.tokopedia.review.feature.bulk_write_review.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.diffutil.BulkReviewDiffUtilCallback
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewBadRatingCategoryAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewBadRatingCategoryViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel

class BulkReviewBadRatingCategoryAdapter(
    listener: BulkReviewBadRatingCategoryViewHolder.Listener
) : BaseAdapter<BulkReviewBadRatingCategoryAdapterTypeFactory>(
    BulkReviewBadRatingCategoryAdapterTypeFactory(listener)
) {
    @Suppress("UNCHECKED_CAST")
    fun setBadRatingCategories(badRatingCategories: List<BulkReviewBadRatingCategoryUiModel>) {
        val diffUtilCallback = BulkReviewDiffUtilCallback(
            visitables.toMutableList() as List<BulkReviewBadRatingCategoryUiModel>,
            badRatingCategories
        )
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(badRatingCategories)
        diffUtilResult.dispatchUpdatesTo(this)
    }
}

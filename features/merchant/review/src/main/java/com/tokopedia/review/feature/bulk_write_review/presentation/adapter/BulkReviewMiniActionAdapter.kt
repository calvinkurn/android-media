package com.tokopedia.review.feature.bulk_write_review.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.diffutil.BulkReviewDiffUtilCallback
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewMiniActionAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewMiniActionViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewMiniActionUiModel

class BulkReviewMiniActionAdapter(
    listener: BulkReviewMiniActionViewHolder.Listener
) : BaseAdapter<BulkReviewMiniActionAdapterTypeFactory>(
    BulkReviewMiniActionAdapterTypeFactory(listener)
) {
    @Suppress("UNCHECKED_CAST")
    fun setMiniActions(miniActions: List<BulkReviewMiniActionUiModel>) {
        val diffUtilCallback = BulkReviewDiffUtilCallback(
            visitables.toMutableList() as List<BulkReviewMiniActionUiModel>,
            miniActions
        )
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(miniActions)
        diffUtilResult.dispatchUpdatesTo(this)
    }
}

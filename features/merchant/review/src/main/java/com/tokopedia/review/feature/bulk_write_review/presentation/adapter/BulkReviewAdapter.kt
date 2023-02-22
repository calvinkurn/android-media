package com.tokopedia.review.feature.bulk_write_review.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.diffutil.BulkReviewDiffUtilCallback
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.typefactory.BulkReviewAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder.BulkReviewItemViewHolder
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable

class BulkReviewAdapter(
    bulkReviewItemListener: BulkReviewItemViewHolder.Listener
) : BaseAdapter<BulkReviewAdapterTypeFactory>(BulkReviewAdapterTypeFactory(bulkReviewItemListener)) {
    @Suppress("UNCHECKED_CAST")
    fun updateItems(visitableList: List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>) {
        val diffUtilCallback = BulkReviewDiffUtilCallback(
            visitables.toMutableList() as List<BulkReviewVisitable<BulkReviewAdapterTypeFactory>>,
            visitableList
        )
        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(visitableList)
        diffUtilResult.dispatchUpdatesTo(this)
    }
}

package com.tokopedia.review.feature.bulk_write_review.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewVisitable

class BulkReviewDiffUtilCallback<T: BaseAdapterTypeFactory>(
    private val oldItems: List<BulkReviewVisitable<T>>,
    private val newItems: List<BulkReviewVisitable<T>>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].areItemsTheSame(newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].areContentsTheSame(newItems[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return oldItems[oldItemPosition].getChangePayload(newItems[newItemPosition])
    }
}

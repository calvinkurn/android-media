package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable

class ReviewMediaThumbnailDiffUtilCallback(
    private val oldItems: List<ReviewMediaThumbnailVisitable>,
    private val newItems: List<ReviewMediaThumbnailVisitable>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.count()
    }

    override fun getNewListSize(): Int {
        return newItems.count()
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem?.areItemsTheSame(newItem) == true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem?.areContentsTheSame(newItem) == true
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem?.getChangePayload(newItem)
    }
}
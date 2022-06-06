package com.tokopedia.review.feature.createreputation.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory

class ReviewBadRatingCategoriesDiffUtilCallback(
    private val oldItems: List<BadRatingCategory>,
    private val newItems: List<BadRatingCategory>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem == newItem
    }
}
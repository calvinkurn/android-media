package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel

class ReviewMediaGalleryDiffUtilCallback(
    private val oldItems: List<MediaItemUiModel>,
    private val newItems: List<MediaItemUiModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem?.areItemTheSame(newItem) == true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem?.areContentsTheSame(newItem) == true
    }
}
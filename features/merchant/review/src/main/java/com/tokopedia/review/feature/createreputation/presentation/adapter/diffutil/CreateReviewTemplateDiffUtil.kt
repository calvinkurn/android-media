package com.tokopedia.review.feature.createreputation.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel

class CreateReviewTemplateDiffUtil(
    private val oldItems: List<CreateReviewTemplateItemUiModel>,
    private val newItems: List<CreateReviewTemplateItemUiModel>
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
        return oldItem?.areItemsTheSame(newItem) == true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems.getOrNull(oldItemPosition)
        val newItem = newItems.getOrNull(newItemPosition)
        return oldItem?.areContentsTheSame(newItem) == true
    }
}
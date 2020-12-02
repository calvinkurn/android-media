package com.tokopedia.product.addedit.category.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.product.addedit.category.presentation.model.CategoryUiModel

class CategoryDiffCallback(
        private val oldCategories: List<CategoryUiModel>,
        private val newCategories: List<CategoryUiModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldCategories.size
    }

    override fun getNewListSize(): Int {
        return newCategories.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCategories[oldItemPosition].categoryId === newCategories[newItemPosition].categoryId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = oldCategories[oldItemPosition]
        val newEmployee = newCategories[newItemPosition]
        return oldEmployee == newEmployee
    }
}

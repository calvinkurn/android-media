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
        val oldCategory = oldCategories[oldItemPosition]
        val newCategory = newCategories[newItemPosition]
        return oldCategory.categoryId == newCategory.categoryId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCategory = oldCategories[oldItemPosition]
        val newCategory = newCategories[newItemPosition]
        return oldCategory == newCategory
    }
}

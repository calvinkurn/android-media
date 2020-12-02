package com.tokopedia.product.addedit.category.presentation.model

import com.tokopedia.core.common.category.domain.model.Category
import com.tokopedia.core.common.category.view.model.CategoryViewModel

object Mapper {
    fun mapCategoryToCategoryUiModel(categories: List<Category>): List<CategoryUiModel> {
        val listCategories = mutableListOf<CategoryUiModel>()
        categories.forEach {
            val uiModel = CategoryUiModel(
                    categoryId = it.id,
                    categoryName =  it.name,
                    child = mapCategoryToCategoryUiModel(it.child)
            )
            listCategories.add(uiModel)
        }
        return listCategories
    }
}
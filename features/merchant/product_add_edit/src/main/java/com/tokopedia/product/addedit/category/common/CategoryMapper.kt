package com.tokopedia.product.addedit.category.common

import com.tokopedia.core.common.category.domain.model.Category
import com.tokopedia.core.common.category.view.model.CategoryViewModel
import com.tokopedia.product.addedit.category.common.Constant.INCREMENT_CATEGORY_LEVEL
import com.tokopedia.product.addedit.category.presentation.model.CategoryUiModel

object CategoryMapper {
    fun mapCategoryToCategoryUiModel(categories: List<Category>, level: Int): List<CategoryUiModel> {
        val listCategories = mutableListOf<CategoryUiModel>()
        categories.forEach {
            val uiModel = CategoryUiModel(
                    categoryId = it.id,
                    categoryName =  it.name,
                    child = mapCategoryToCategoryUiModel(it.child, INCREMENT_CATEGORY_LEVEL + level),
                    categoryLevel = level
            )
            listCategories.add(uiModel)
        }
        return listCategories
    }

    fun mapCategoryUiModelToCategoryModel(categories: List<CategoryUiModel>): List<CategoryViewModel> {
        val listCategories = mutableListOf<CategoryViewModel>()
        categories.forEach {
            if (it.categoryId.isNotBlank()) {
                val category = CategoryViewModel().apply {
                    id = it.categoryId.toLong()
                    name = it.categoryName
                    isHasChild = it.child.isNotEmpty()
                }
                listCategories.add(category)
            }
        }
        return listCategories
    }
}
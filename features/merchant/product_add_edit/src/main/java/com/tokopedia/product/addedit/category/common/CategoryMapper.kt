package com.tokopedia.product.addedit.category.common

import com.tokopedia.product.manage.common.feature.category.model.Category
import com.tokopedia.product.manage.common.feature.category.model.CategoryUIModel
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

    fun mapCategoryUiModelToCategoryModel(categories: List<CategoryUiModel>): List<CategoryUIModel> {
        val listCategories = mutableListOf<CategoryUIModel>()
        categories.forEach {
            if (it.categoryId.isNotBlank()) {
                val category = CategoryUIModel().apply {
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

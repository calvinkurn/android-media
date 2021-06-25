package com.tokopedia.tokomart.categorylist.domain.mapper

import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel.*
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.unifyprinciples.Typography

object CategoryListMapper {

    fun mapToUiModel(response: List<CategoryResponse>): List<CategoryListItemUiModel> {
        return response.map {
            val childList = mapToChildUiModel(it)
            CategoryListItemUiModel(it.id, it.name, it.imageUrl, it.appLinks, childList)
        }
    }

    private fun mapToChildUiModel(category: CategoryResponse): List<CategoryListChildUiModel> {
        val categoryList = mutableListOf<CategoryListChildUiModel>()

        val childList = category.childList?.map {
            CategoryListChildUiModel(it.id, it.name, it.imageUrl, it.appLinks)
        }.orEmpty()

        val allCategoryTextUiModel = CategoryListChildUiModel(
            id = category.id,
            name = category.name,
            textWeight = Typography.BOLD,
            textColorId = com.tokopedia.unifyprinciples.R.color.Unify_G500,
            appLink = category.appLinks,
            type = CategoryType.ALL_CATEGORY_TEXT
        )

        categoryList.addAll(childList)
        categoryList.add(allCategoryTextUiModel)

        return categoryList
    }
}

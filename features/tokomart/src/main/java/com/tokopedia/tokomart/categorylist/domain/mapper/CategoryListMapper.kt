package com.tokopedia.tokomart.categorylist.domain.mapper

import com.tokopedia.tokomart.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.unifyprinciples.Typography

object CategoryListMapper {

    fun mapToUiModel(response: List<CategoryListResponse>): List<CategoryListItemUiModel> {
        return response.map {
            val childList = mapToChildUiModel(it)
            CategoryListItemUiModel(it.id, it.title, it.iconUrl, childList)
        }
    }

    private fun mapToChildUiModel(category: CategoryListResponse, ): List<CategoryListChildUiModel> {
        val categoryList = mutableListOf<CategoryListChildUiModel>()

        val childList = category.childList?.map {
            CategoryListChildUiModel(it.id, it.title, it.iconUrl)
        }.orEmpty()

        val allCategoryTextUiModel = CategoryListChildUiModel(
            id = category.id,
            title = "Semua Produk ${category.title}",
            textWeight = Typography.BOLD,
            textColorId = com.tokopedia.unifyprinciples.R.color.Unify_G500
        )

        categoryList.addAll(childList)
        categoryList.add(allCategoryTextUiModel)

        return categoryList
    }
}

package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListChildUiModel.*
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.seeallcategories.persentation.uimodel.SeeAllCategoriesItemUiModel
import com.tokopedia.unifyprinciples.Typography

object CategoryListMapper {

    fun mapToUiModel(response: List<GetCategoryListResponse.CategoryListResponse.CategoryResponse>): List<CategoryListItemUiModel> {
        return response.map {
            val childList = mapToChildUiModel(it)
            CategoryListItemUiModel(it.id, it.name, it.imageUrl, it.appLinks, childList)
        }
    }

    fun GetCategoryListResponse.CategoryListResponse.mapToSeeAllCategoriesItemUiModel(): List<SeeAllCategoriesItemUiModel> = data.map {
        SeeAllCategoriesItemUiModel(
            id = it.id,
            name = it.name,
            appLink = it.appLinks,
            imageUrl = it.imageUrl
        )
    }

    private fun mapToChildUiModel(category: GetCategoryListResponse.CategoryListResponse.CategoryResponse): List<CategoryListChildUiModel> {
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

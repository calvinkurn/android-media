package com.tokopedia.tokopedianow.common.domain.mapper

import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListChildUiModel.*
import com.tokopedia.tokopedianow.categorylist.presentation.uimodel.CategoryListItemUiModel
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.seeallcategory.persentation.uimodel.SeeAllCategoryItemUiModel
import com.tokopedia.unifyprinciples.Typography

object CategoryListMapper {

    fun mapToUiModel(response: List<GetCategoryListResponse.CategoryListResponse.CategoryResponse>): List<CategoryListItemUiModel> {
        return response.map {
            val childList = mapToChildUiModel(it)
            CategoryListItemUiModel(it.id, it.name, it.imageUrl, it.appLinks, childList)
        }
    }

    fun GetCategoryListResponse.CategoryListResponse.mapToSeeAllCategoryItemUiModel(): List<SeeAllCategoryItemUiModel> = data.map {
        SeeAllCategoryItemUiModel(
            id = it.id,
            name = it.name,
            appLink = it.appLinks,
            imageUrl = it.imageUrl,
            color = it.color
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
            textColorId = com.tokopedia.unifyprinciples.R.color.Unify_GN500,
            appLink = category.appLinks,
            type = CategoryType.ALL_CATEGORY_TEXT
        )

        categoryList.addAll(childList)
        categoryList.add(allCategoryTextUiModel)

        return categoryList
    }
}

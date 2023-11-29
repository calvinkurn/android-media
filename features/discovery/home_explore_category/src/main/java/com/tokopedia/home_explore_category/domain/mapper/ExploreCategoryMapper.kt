package com.tokopedia.home_explore_category.domain.mapper

import com.tokopedia.home_explore_category.domain.model.GetExploreCategoryResponse
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import javax.inject.Inject

class ExploreCategoryMapper @Inject constructor() {

    fun mapToExploreCategoryUiModelList(
        exploreCategoryResponse: GetExploreCategoryResponse
    ): ExploreCategoryResultUiModel {
        return ExploreCategoryResultUiModel(
            exploreCategoryResponse.dynamicHomeIcon.categoryGroup.map {
                ExploreCategoryUiModel(
                    id = it.id,
                    categoryTitle = it.title,
                    categoryImageUrl = it.imageUrl,
                    subExploreCategoryList = it.categoryRows.map { categoryRow ->
                        ExploreCategoryUiModel.SubExploreCategoryUiModel(
                            id = categoryRow.id,
                            name = categoryRow.name,
                            imageUrl = categoryRow.imageUrl,
                            appLink = categoryRow.applinks,
                            categoryLabel = categoryRow.categoryLabel,
                            buIdentifier = categoryRow.buIdentifier
                        )
                    }
                )
            })
    }
}

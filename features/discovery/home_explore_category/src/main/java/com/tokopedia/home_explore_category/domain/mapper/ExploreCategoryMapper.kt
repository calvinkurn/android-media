package com.tokopedia.home_explore_category.domain.mapper

import com.tokopedia.home_explore_category.domain.model.GetExploreCategoryResponse
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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
            }
        )
    }

    fun mapToExploreCategoryUiModelListTemp(
        exploreCategoryResponse: GetExploreCategoryResponse
    ): ExploreCategoryResultUiModel {
        val exploreCategoryList =
            exploreCategoryResponse.dynamicHomeIcon.categoryGroup.flatMap { exploreCategory ->
                List(20) { position ->
                    ExploreCategoryUiModel(
                        id = (exploreCategory.id.toIntOrZero() + (position+Int.ONE)).toString(),
                        categoryTitle = exploreCategory.title,
                        categoryImageUrl = exploreCategory.imageUrl,
                        subExploreCategoryList = exploreCategory.categoryRows.flatMap { categoryRow ->
                            List(6) { subPosition ->
                                ExploreCategoryUiModel.SubExploreCategoryUiModel(
                                    id = (categoryRow.id.toIntOrZero() + (subPosition+Int.ONE)).toString(),
                                    name = categoryRow.name,
                                    imageUrl = categoryRow.imageUrl,
                                    appLink = categoryRow.applinks,
                                    categoryLabel = categoryRow.categoryLabel,
                                    buIdentifier = categoryRow.buIdentifier
                                )
                            }
                        }
                    )
                }
            }

        return ExploreCategoryResultUiModel(exploreCategoryList)
    }
}

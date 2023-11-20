package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse.CategoryDetail
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel

object CategoryRecommendationMapper {
    fun CategoryDetailResponse.mapToCategoryRecommendation(source: String): TokoNowCategoryMenuUiModel {
        return categoryDetail.mapToCategoryRecommendation(source)
    }

    fun CategoryDetail.mapToCategoryRecommendation(source: String): TokoNowCategoryMenuUiModel {
        return TokoNowCategoryMenuUiModel(
            categoryListUiModel = data.recommendation.map {
                TokoNowCategoryMenuItemUiModel(
                    id = it.id,
                    title = it.name,
                    imageUrl = it.imageUrl,
                    appLink = it.applinks,
                    color = it.colorObj.hexLight,
                    headerName = String.EMPTY,
                )
            },
            state = TokoNowLayoutState.SHOW,
            source = source
        )
    }
}

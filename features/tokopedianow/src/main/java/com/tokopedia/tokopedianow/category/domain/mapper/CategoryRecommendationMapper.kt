package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_L1

object CategoryRecommendationMapper {
    fun CategoryDetailResponse.mapToCategoryRecommendation(): TokoNowCategoryMenuUiModel = TokoNowCategoryMenuUiModel(
        categoryListUiModel = categoryDetail.data.recommendation.map {
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
        source = TOKONOW_CATEGORY_L1
    )
}

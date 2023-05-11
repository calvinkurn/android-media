package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.domain.response.CategoryModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel

object CategoryNavigationMapper {
    fun CategoryModel.mapToCategoryNavigation(
        categoryIdL1: String
    ): TokoNowCategoryMenuUiModel = TokoNowCategoryMenuUiModel(
        categoryListUiModel = categoryNavigation.data.find { it.id == categoryIdL1 }?.childList?.map {
            TokoNowCategoryMenuItemUiModel(
                id = it.id,
                title = it.name,
                imageUrl = it.imageUrl,
                appLink = it.appLinks,
                color = it.color,
                headerName = String.EMPTY
            )
        },
        state = TokoNowLayoutState.SHOW
    )
}

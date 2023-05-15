package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse

internal object CategoryNavigationMapper {
    fun CategoryListResponse.mapToCategoryNavigation(
        categoryIdL1: String
    ): CategoryNavigationUiModel = CategoryNavigationUiModel(
        categoryListUiModel = data.find { it.id == categoryIdL1 }?.childList?.map {
            CategoryNavigationItemUiModel(
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

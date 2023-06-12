package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationItemUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

internal object CategoryNavigationMapper {
    fun CategoryDetailResponse.mapToCategoryNavigation(): CategoryNavigationUiModel = CategoryNavigationUiModel(
        categoryListUiModel = categoryDetail.data.child.map {
            CategoryNavigationItemUiModel(
                id = it.id,
                title = it.name,
                imageUrl = it.imageUrl,
                appLink = it.applinks,
                backgroundLightColor = it.color,
                headerName = String.EMPTY
            )
        },
        state = TokoNowLayoutState.SHOW
    )
}

package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel

internal object CategoryMenuMapper {
    fun GetCategoryListResponse.CategoryListResponse.mapToCategoryMenu(): TokoNowCategoryMenuUiModel = TokoNowCategoryMenuUiModel(
        categoryListUiModel = data.map {
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

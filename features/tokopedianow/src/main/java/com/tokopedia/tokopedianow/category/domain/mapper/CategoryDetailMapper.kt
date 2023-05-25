package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

internal object CategoryDetailMapper {
    fun CategoryDetailResponse.mapToHeaderSpace(space: Int): CategoryHeaderSpaceUiModel = CategoryHeaderSpaceUiModel(
        space = space,
        backgroundColor = categoryDetail.data.color
    )

    fun CategoryDetailResponse.mapToChooseAddress(): TokoNowChooseAddressWidgetUiModel = TokoNowChooseAddressWidgetUiModel(
        backgroundColor = categoryDetail.data.color
    )

    fun CategoryDetailResponse.mapToCategoryTitle(): CategoryTitleUiModel = CategoryTitleUiModel(
        title = categoryDetail.data.name,
        backgroundColor = categoryDetail.data.color
    )
}

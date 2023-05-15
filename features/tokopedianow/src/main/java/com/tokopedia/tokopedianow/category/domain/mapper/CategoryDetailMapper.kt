package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.tokopedianow.category.domain.response.CategoryHeaderResponse
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

internal object CategoryDetailMapper {
    fun CategoryHeaderResponse.mapToHeaderSpace(space: Int): CategoryHeaderSpaceUiModel = CategoryHeaderSpaceUiModel(
        space = space,
        backgroundColor = categoryDetail.data.color
    )

    fun CategoryHeaderResponse.mapToChooseAddress(): TokoNowChooseAddressWidgetUiModel = TokoNowChooseAddressWidgetUiModel(
        backgroundColor = categoryDetail.data.color
    )

    fun CategoryHeaderResponse.mapToCategoryTitle(): CategoryTitleUiModel = CategoryTitleUiModel(
        title = categoryDetail.data.name,
        backgroundColor = categoryDetail.data.color
    )
}

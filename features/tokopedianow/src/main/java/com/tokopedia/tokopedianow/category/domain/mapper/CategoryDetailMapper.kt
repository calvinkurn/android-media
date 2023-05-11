package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.category.domain.response.CategoryModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel

object CategoryDetailMapper {
    fun CategoryModel.mapToHeaderSpace(space: Int): CategoryHeaderSpaceUiModel = CategoryHeaderSpaceUiModel(
        space = space,
        backgroundColor = categoryDetail.data.color
    )

    fun CategoryModel.mapToChooseAddress(): TokoNowChooseAddressWidgetUiModel = TokoNowChooseAddressWidgetUiModel(
        backgroundColor = categoryDetail.data.color
    )

    fun CategoryModel.mapToCategoryTitle(): CategoryTitleUiModel = CategoryTitleUiModel(
        title = categoryDetail.data.name,
        backgroundColor = categoryDetail.data.color
    )
}

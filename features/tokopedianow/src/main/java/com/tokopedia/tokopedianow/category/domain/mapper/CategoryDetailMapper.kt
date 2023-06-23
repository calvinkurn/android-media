package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.unifycomponents.ticker.TickerData

internal object CategoryDetailMapper {
    fun CategoryDetailResponse.mapToHeaderSpace(space: Int): CategoryHeaderSpaceUiModel = CategoryHeaderSpaceUiModel(
        space = space,
        backgroundLightColor = categoryDetail.data.color
    )

    fun CategoryDetailResponse.mapToChooseAddress(): TokoNowChooseAddressWidgetUiModel = TokoNowChooseAddressWidgetUiModel(
        backgroundLightColor = categoryDetail.data.color
    )

    fun CategoryDetailResponse.mapToCategoryTitle(): CategoryTitleUiModel = CategoryTitleUiModel(
        title = categoryDetail.data.name,
        backgroundLightColor = categoryDetail.data.color
    )

    fun CategoryDetailResponse.mapToTicker(
        tickerData: Pair<Boolean, List<TickerData>>
    ) = TokoNowTickerUiModel(
        tickers = tickerData.second,
        backgroundLightColor = categoryDetail.data.color
    )
}

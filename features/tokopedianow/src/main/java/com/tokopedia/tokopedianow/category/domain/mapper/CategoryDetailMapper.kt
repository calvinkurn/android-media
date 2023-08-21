package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.unifycomponents.ticker.TickerData

internal object CategoryDetailMapper {
    fun CategoryDetailResponse.mapToHeaderSpace(space: Int): CategoryHeaderSpaceUiModel = CategoryHeaderSpaceUiModel(
        space = space,
        backgroundLightColor = categoryDetail.data.colorObj.hexLight,
        backgroundDarkColor = categoryDetail.data.colorObj.hexDark
    )

    fun CategoryDetailResponse.mapToChooseAddress(
        addressData: LocalCacheModel
    ): TokoNowChooseAddressWidgetUiModel = TokoNowChooseAddressWidgetUiModel(
        backgroundLightColor = categoryDetail.data.colorObj.hexLight,
        backgroundDarkColor = categoryDetail.data.colorObj.hexDark,
        addressData = addressData
    )

    fun CategoryDetailResponse.mapToCategoryTitle(): CategoryTitleUiModel = CategoryTitleUiModel(
        title = categoryDetail.data.name,
        backgroundLightColor = categoryDetail.data.colorObj.hexLight,
        backgroundDarkColor = categoryDetail.data.colorObj.hexDark
    )

    fun CategoryDetailResponse.mapToTicker(
        tickerList: List<TickerData>
    ) = TokoNowTickerUiModel(
        tickers = tickerList,
        backgroundLightColor = categoryDetail.data.colorObj.hexLight,
        backgroundDarkColor = categoryDetail.data.colorObj.hexDark
    )
}

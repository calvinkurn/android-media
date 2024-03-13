package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.common.model.TokoNowThematicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.unifycomponents.ticker.TickerData

internal object CategoryDetailMapper {
    fun CategoryDetailResponse.mapToChooseAddress(
        localCacheModel: LocalCacheModel
    ): TokoNowChooseAddressWidgetUiModel = TokoNowChooseAddressWidgetUiModel(
        backgroundLightColor = categoryDetail.data.colorObj.hexLight,
        backgroundDarkColor = categoryDetail.data.colorObj.hexDark,
        localCacheModel = localCacheModel
    )

    fun CategoryDetailResponse.mapToCategoryHeader(
        ctaText: String,
        ctaTextColor: Int,
        tickerList: List<TickerData>
    ): TokoNowThematicHeaderUiModel = TokoNowThematicHeaderUiModel(
        pageTitle = categoryDetail.data.name,
        backgroundLightColor = categoryDetail.data.colorObj.hexLight,
        backgroundDarkColor = categoryDetail.data.colorObj.hexDark,
        chosenAddress = TokoNowThematicHeaderUiModel.ChosenAddress(isShown = true),
        ctaText = ctaText,
        ctaTextColor = ctaTextColor,
        ticker = TokoNowThematicHeaderUiModel.Ticker(tickerList)
    )
}

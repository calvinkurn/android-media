package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.tokopedianow.common.model.TokoNowRecentPurchaseUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.*

interface HomeTypeFactory {
    fun type(uiModel: HomeChooseAddressWidgetUiModel): Int
    fun type(uiModel: HomeTickerUiModel): Int
    fun type(uiModel: HomeProductRecomUiModel): Int
    fun type(uiModel: HomeEmptyStateUiModel): Int
    fun type(uiModel: HomeLoadingStateUiModel): Int
    fun type(uiModel: TokoNowRecentPurchaseUiModel): Int
}

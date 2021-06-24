package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.tokopedianow.home.presentation.uimodel.*

interface TokoMartHomeTypeFactory {
    fun type(uiModel: HomeChooseAddressWidgetUiModel): Int
    fun type(uiModel: HomeCategoryGridUiModel): Int
    fun type(uiModel: HomeCategoryItemUiModel): Int
    fun type(uiModel: HomeTickerUiModel): Int
    fun type(uiModel: HomeEmptyStateUiModel): Int
    fun type(uiModel: HomeLoadingStateUiModel): Int
}

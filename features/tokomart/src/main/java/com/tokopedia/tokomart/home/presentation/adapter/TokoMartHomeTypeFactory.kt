package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.tokomart.home.presentation.uimodel.*

interface TokoMartHomeTypeFactory {
    fun type(uiModel: HomeChooseAddressWidgetUiModel): Int
    fun type(uiModel: HomeCategoryGridUiModel): Int
    fun type(uiModel: HomeCategoryItemUiModel): Int
    fun type(uiModel: HomeTickerUiModel): Int
}

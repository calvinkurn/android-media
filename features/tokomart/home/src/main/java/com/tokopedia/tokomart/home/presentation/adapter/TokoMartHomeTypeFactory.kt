package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.tokomart.home.presentation.uimodel.*

interface TokoMartHomeTypeFactory {

    fun type(uiModel: HomeSectionUiModel): Int
    fun type(uiModel: HomeAllCategoryUiModel): Int
    fun type(uiModel: HomeSliderBannerUiModel): Int
    fun type(uiModel: HomeSliderProductUiModel): Int
    fun type(uiModel: HomeChooseAddressWidgetUiModel): Int
//    fun type(uiModel: HomeSpecialRecipeUiModel): Int NOT MVP
}

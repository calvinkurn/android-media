package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.tokomart.home.constant.HomeLayoutType
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.*

object HomeLayoutMapper {

    private val SUPPORTED_TYPE = listOf(
        HomeLayoutType.SECTION,
        HomeLayoutType.ALL_CATEGORY,
        HomeLayoutType.DYNAMIC_CHANNEL,
        HomeLayoutType.SLIDER_BANNER,
        HomeLayoutType.SLIDER_PRODUCT,
        HomeLayoutType.CHOOSE_ADDRESS_WIDGET,
        HomeLayoutType.SLIDER_PRODUCT_PERSONALIZED
    )

    fun mapToHomeUiModel(response: List<HomeLayoutResponse>): List<HomeLayoutUiModel> {
        return response.filter { SUPPORTED_TYPE.contains(it.type) }.map {
            when(it.type) {
                HomeLayoutType.ALL_CATEGORY -> HomeAllCategoryUiModel(it.id, it.title)
                HomeLayoutType.SLIDER_BANNER -> HomeSliderBannerUiModel(null, false)
                HomeLayoutType.SLIDER_PRODUCT -> HomeSliderProductUiModel()
                HomeLayoutType.CHOOSE_ADDRESS_WIDGET -> HomeChooseAddressWidgetUiModel(false)
                HomeLayoutType.SLIDER_PRODUCT_PERSONALIZED -> HomeSliderProductPersonalizedUiModel()
                else -> HomeSectionUiModel(it.id, it.title)
            }
        }
    }
}

package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.home.constant.HomeLayoutType
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.TICKER_WIDGET_ID
import com.tokopedia.tokomart.home.domain.mapper.HomeCategoryMapper.mapToCategoryLayout
import com.tokopedia.tokomart.home.domain.mapper.HomeCategoryMapper.mapToCategoryList
import com.tokopedia.tokomart.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.tokomart.home.domain.mapper.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.tokomart.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.HomeChooseAddressWidgetUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeCategoryGridUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeEmptyStateUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeTickerUiModel
import com.tokopedia.unifycomponents.ticker.TickerData

object HomeLayoutMapper {

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        HomeLayoutType.CATEGORY,
        HomeLayoutType.LEGO_3_IMAGE,
        HomeLayoutType.LEGO_6_IMAGE,
        HomeLayoutType.BANNER_CAROUSEL
    )

    fun addChooseAddressIntoList(): List<Visitable<*>> {
        val layoutList = mutableListOf<Visitable<*>>()
        layoutList.add(HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID))
        return layoutList
    }

    fun addEmptyStateIntoList(id: String): List<Visitable<*>> {
        val layoutList = mutableListOf<Visitable<*>>()
        layoutList.add(HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID))
        layoutList.add(HomeEmptyStateUiModel(id = id))
        return layoutList
    }

    fun mapHomeLayoutList(response: List<HomeLayoutResponse>, tickers: List<TickerData>): List<Visitable<*>> {
        val layoutList = mutableListOf<Visitable<*>>()
        layoutList.add(HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID))

        if (!tickers.isNullOrEmpty()) {
            layoutList.add(HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers))
        }

        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach {
            mapToHomeUiModel(it)?.let { item ->
                layoutList.add(item)
            }
        }
        return layoutList
    }

    fun List<Visitable<*>>.mapGlobalHomeLayoutData(
        item: HomeComponentVisitable,
        response: HomeLayoutResponse
    ): List<Visitable<*>> {
        return updateItemById(item.visitableId()) {
            mapToHomeUiModel(response)
        }
    }

    fun List<Visitable<*>>.mapHomeCategoryGridData(
        item: HomeCategoryGridUiModel,
        response: List<CategoryResponse>
    ): List<Visitable<*>> {
        return updateItemById(item.visitableId) {
            val categoryList = mapToCategoryList(response)
            item.copy(categoryList = categoryList)
        }
    }

    private fun mapToHomeUiModel(response: HomeLayoutResponse): Visitable<*>? {
        return when (response.layout) {
            HomeLayoutType.CATEGORY -> mapToCategoryLayout(response)
            HomeLayoutType.LEGO_3_IMAGE,
            HomeLayoutType.LEGO_6_IMAGE -> mapLegoBannerDataModel(response)
            HomeLayoutType.BANNER_CAROUSEL -> mapSliderBannerModel(response)
            else -> null
        }
    }
}

package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.CATEGORY
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.LEGO_3_IMAGE
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.TICKER_WIDGET_ID
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryList
import com.tokopedia.tokopedianow.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.unifycomponents.ticker.TickerData

object HomeLayoutMapper {

    /**
     * List of layout IDs that doesn't need to call GQL query from Toko Now Home
     * to fetch the data. For example: Choose Address Widget. The GQL call for
     * Choose Address Widget data is done internally, so Toko Now Home doesn't
     * need to call query to fetch data for it.
     */
    private val STATIC_LAYOUT_ID = listOf(
            CHOOSE_ADDRESS_WIDGET_ID,
            TICKER_WIDGET_ID,
            EMPTY_STATE_NO_ADDRESS,
            EMPTY_STATE_FAILED_TO_FETCH_DATA
    )

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        CATEGORY,
        LEGO_3_IMAGE,
        LEGO_6_IMAGE,
        BANNER_CAROUSEL
    )

    fun addLoadingIntoList(): List<Visitable<*>> {
        val layoutList = mutableListOf<Visitable<*>>()
        layoutList.add(HomeLoadingStateUiModel(id = LOADING_STATE))
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
        response: List<CategoryResponse>?
    ): List<Visitable<*>> {
        return updateItemById(item.visitableId) {
            if (!response.isNullOrEmpty()) {
                val categoryList = mapToCategoryList(response)
                item.copy(categoryList = categoryList, state = HomeLayoutState.SHOW)
            } else {
                item.copy(categoryList = null, state = HomeLayoutState.HIDE)
            }
        }
    }

    fun Visitable<*>.isNotStaticLayout(): Boolean {
        return this.getVisitableId() !in STATIC_LAYOUT_ID
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when(this) {
            is HomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            else -> null
        }
    }

    private fun mapToHomeUiModel(response: HomeLayoutResponse): Visitable<*>? {
        return when (response.layout) {
            CATEGORY -> mapToCategoryLayout(response)
            LEGO_3_IMAGE, LEGO_6_IMAGE -> mapLegoBannerDataModel(response)
            BANNER_CAROUSEL -> mapSliderBannerModel(response)
            else -> null
        }
    }
}

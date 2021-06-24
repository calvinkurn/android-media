package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.home.constant.HomeLayoutItemState
import com.tokopedia.tokomart.home.constant.HomeLayoutState
import com.tokopedia.tokomart.home.constant.HomeLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.tokomart.home.constant.HomeLayoutType.Companion.CATEGORY
import com.tokopedia.tokomart.home.constant.HomeLayoutType.Companion.LEGO_3_IMAGE
import com.tokopedia.tokomart.home.constant.HomeLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokomart.home.constant.HomeStaticLayoutId.Companion.TICKER_WIDGET_ID
import com.tokopedia.tokomart.home.domain.mapper.HomeCategoryMapper.mapToCategoryLayout
import com.tokopedia.tokomart.home.domain.mapper.HomeCategoryMapper.mapToCategoryList
import com.tokopedia.tokomart.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.tokomart.home.domain.mapper.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.tokomart.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokomart.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokomart.home.presentation.uimodel.*
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

    fun addLoadingIntoList(): List<HomeLayoutItemUiModel> {
        val layoutList = mutableListOf<HomeLayoutItemUiModel>()
        val loadingLayout = HomeLoadingStateUiModel(id = LOADING_STATE)
        layoutList.add(HomeLayoutItemUiModel(loadingLayout, HomeLayoutItemState.LOADED))
        return layoutList
    }

    fun addEmptyStateIntoList(id: String): List<HomeLayoutItemUiModel> {
        val layoutList = mutableListOf<HomeLayoutItemUiModel>()
        val chooseAddressUiModel = HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        val homeEmptyStateUiModel = HomeEmptyStateUiModel(id = id)
        layoutList.add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))
        layoutList.add(HomeLayoutItemUiModel(homeEmptyStateUiModel, HomeLayoutItemState.LOADED))
        return layoutList
    }

    fun mapHomeLayoutList(response: List<HomeLayoutResponse>, tickers: List<TickerData>): List<HomeLayoutItemUiModel> {
        val layoutList = mutableListOf<HomeLayoutItemUiModel>()
        val chooseAddressUiModel = HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        layoutList.add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))

        if (!tickers.isNullOrEmpty()) {
            val ticker = HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = tickers)
            layoutList.add(HomeLayoutItemUiModel(ticker, HomeLayoutItemState.LOADED))
        }

        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach {
            mapToHomeUiModel(it)?.let { item ->
                layoutList.add(item)
            }
        }
        return layoutList
    }

    fun List<HomeLayoutItemUiModel>.mapGlobalHomeLayoutData(
        item: HomeComponentVisitable,
        response: HomeLayoutResponse
    ): List<HomeLayoutItemUiModel> {
        return updateItemById(item.visitableId()) {
            mapToHomeUiModel(response, HomeLayoutItemState.LOADED)
        }
    }

    fun List<HomeLayoutItemUiModel>.updateStateToLoading(item: HomeLayoutItemUiModel): List<HomeLayoutItemUiModel> {
        val layout = item.layout
        return updateItemById(layout.getVisitableId()) {
            HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADING)
        }
    }

    fun List<HomeLayoutItemUiModel>.mapHomeCategoryGridData(
        item: HomeCategoryGridUiModel,
        response: List<CategoryResponse>?
    ): List<HomeLayoutItemUiModel> {
        return updateItemById(item.visitableId) {
            if (!response.isNullOrEmpty()) {
                val categoryList = mapToCategoryList(response)
                val layout = item.copy(categoryList = categoryList, state = HomeLayoutState.SHOW)
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED)
            } else {
                val layout = item.copy(categoryList = null, state = HomeLayoutState.HIDE)
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED)
            }
        }
    }

    fun Visitable<*>.isNotStaticLayout(): Boolean {
        return this.getVisitableId() !in STATIC_LAYOUT_ID
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when(this) {
            is TokoMartHomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            else -> null
        }
    }

    private fun mapToHomeUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState = HomeLayoutItemState.NOT_LOADED
    ): HomeLayoutItemUiModel? {
        return when (response.layout) {
            CATEGORY -> mapToCategoryLayout(response, state)
            LEGO_3_IMAGE, LEGO_6_IMAGE -> mapLegoBannerDataModel(response, state)
            BANNER_CAROUSEL -> mapSliderBannerModel(response, state)
            else -> null
        }
    }
}

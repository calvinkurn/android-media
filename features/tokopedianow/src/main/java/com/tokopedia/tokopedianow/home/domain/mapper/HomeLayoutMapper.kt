package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.model.TokoNowCategoryGridUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.CATEGORY
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.LEGO_3_IMAGE
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.tokopedianow.home.constant.HomeLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.TICKER_WIDGET_ID
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryLayout
import com.tokopedia.tokopedianow.home.domain.mapper.HomeCategoryMapper.mapToCategoryList
import com.tokopedia.tokopedianow.home.domain.mapper.LegoBannerMapper.mapLegoBannerDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.ProductRecomMapper.mapProductRecomDataModel
import com.tokopedia.tokopedianow.home.domain.mapper.SliderBannerMapper.mapSliderBannerModel
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.getItemIndex
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
            EMPTY_STATE_NO_ADDRESS,
            EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE,
            EMPTY_STATE_FAILED_TO_FETCH_DATA
    )

    private val SUPPORTED_LAYOUT_TYPES = listOf(
        CATEGORY,
        LEGO_3_IMAGE,
        LEGO_6_IMAGE,
        BANNER_CAROUSEL,
        PRODUCT_RECOM
    )

    fun MutableList<HomeLayoutItemUiModel>.addLoadingIntoList() {
        val loadingLayout = HomeLoadingStateUiModel(id = LOADING_STATE)
        add(HomeLayoutItemUiModel(loadingLayout, HomeLayoutItemState.LOADED))
    }

    fun MutableList<HomeLayoutItemUiModel>.addEmptyStateIntoList(id: String) {
        val chooseAddressUiModel = HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        val homeEmptyStateUiModel = HomeEmptyStateUiModel(id = id)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))
        add(HomeLayoutItemUiModel(homeEmptyStateUiModel, HomeLayoutItemState.LOADED))
    }

    fun MutableList<HomeLayoutItemUiModel>.mapHomeLayoutList(response: List<HomeLayoutResponse>, hasTickerBeenRemoved: Boolean) {
        val chooseAddressUiModel = HomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))

        if(!hasTickerBeenRemoved) {
            val ticker = HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = emptyList())
            add(HomeLayoutItemUiModel(ticker, HomeLayoutItemState.NOT_LOADED))
        }

        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach {
            mapToHomeUiModel(it)?.let { item ->
                add(item)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapGlobalHomeLayoutData(item: HomeComponentVisitable, response: HomeLayoutResponse) {
        updateItemById(item.visitableId()) { mapToHomeUiModel(response, HomeLayoutItemState.LOADED) }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapGlobalHomeLayoutData(item: HomeLayoutUiModel, response: HomeLayoutResponse) {
        updateItemById(item.visitableId) { mapToHomeUiModel(response, HomeLayoutItemState.LOADED) }
    }

    fun MutableList<HomeLayoutItemUiModel>.updateStateToLoading(item: HomeLayoutItemUiModel) {
        item.layout.let { layout ->
            updateItemById(layout.getVisitableId()) {
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADING)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapHomeCategoryGridData(item: TokoNowCategoryGridUiModel, response: List<CategoryResponse>?) {
        updateItemById(item.visitableId) {
            if (!response.isNullOrEmpty()) {
                val categoryList = mapToCategoryList(response)
                val layout = item.copy(categoryList = categoryList, state = TokoNowLayoutState.SHOW)
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED)
            } else {
                val layout = item.copy(categoryList = null, state = TokoNowLayoutState.HIDE)
                HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapTickerData(item: HomeTickerUiModel, tickerData: List<TickerData>) {
        updateItemById(item.visitableId) {
            val ticker = HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = tickerData)
            HomeLayoutItemUiModel(ticker, HomeLayoutItemState.LOADED)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.removeItem(id: String) {
        getItemIndex(id)?.let { removeAt(it) }
    }

    fun MutableList<HomeLayoutItemUiModel>.findNextIndex(): Int? {
        return firstOrNull { it.state == HomeLayoutItemState.NOT_LOADED }?.let { indexOf(it) }
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

    private fun mapToHomeUiModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState = HomeLayoutItemState.NOT_LOADED
    ): HomeLayoutItemUiModel? {
        return when (response.layout) {
            CATEGORY -> mapToCategoryLayout(response, state)
            LEGO_3_IMAGE, LEGO_6_IMAGE -> mapLegoBannerDataModel(response, state)
            BANNER_CAROUSEL -> mapSliderBannerModel(response, state)
            PRODUCT_RECOM -> mapProductRecomDataModel(response, state)
            else -> null
        }
    }
}

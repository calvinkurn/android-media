package com.tokopedia.dilayanitokopedia.home.domain.mapper

import com.tokopedia.dilayanitokopedia.common.model.DtChooseAddressWidgetUiModel
import com.tokopedia.dilayanitokopedia.home.constant.HomeLayoutItemState
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId
import com.tokopedia.dilayanitokopedia.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.dilayanitokopedia.home.uimodel.HomeLayoutItemUiModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

object HomeLayoutMapper {


//    fun MutableList<HomeLayoutItemUiModel>.addLoadingIntoList() {
//        val loadingLayout = HomeLoadingStateUiModel(id = LOADING_STATE)
//        add(HomeLayoutItemUiModel(loadingLayout, HomeLayoutItemState.LOADED))
//    }

    fun MutableList<HomeLayoutItemUiModel>.addEmptyStateIntoList(
        @HomeStaticLayoutId id: String,
        serviceType: String
    ) {
        val chooseAddressUiModel = DtChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))
//        when (id) {
//            EMPTY_STATE_OUT_OF_COVERAGE -> {
//                val layout = TokoNowEmptyStateOocUiModel(id, SOURCE, serviceType)
//                add(HomeLayoutItemUiModel(layout, HomeLayoutItemState.LOADED))
//            }
//            EMPTY_STATE_FAILED_TO_FETCH_DATA -> {
//                add(HomeLayoutItemUiModel(TokoNowServerErrorUiModel, HomeLayoutItemState.LOADED))
//            }
//            else -> {
//                add(HomeLayoutItemUiModel(HomeEmptyStateUiModel(id), HomeLayoutItemState.LOADED))
//            }
//        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapHomeLayoutList(
//        response: List<HomeLayoutResponse>,
//        hasTickerBeenRemoved: Boolean,
//        removeAbleWidgets: List<HomeRemoveAbleWidget>,
//        miniCartData: MiniCartSimplifiedData?,
        localCacheModel: LocalCacheModel,
//        isLoggedIn: Boolean
    ) {
        val chooseAddressUiModel = DtChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))

//        if (!hasTickerBeenRemoved) {
//            val ticker = HomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = emptyList())
//            add(HomeLayoutItemUiModel(ticker, HomeLayoutItemState.NOT_LOADED))
//        }

//        response.filter { SUPPORTED_LAYOUT_TYPES.contains(it.layout) }.forEach { layoutResponse ->
//            if (removeAbleWidgets.none { layoutResponse.layout == it.type && it.isRemoved }) {
//
//                mapToHomeUiModel(layoutResponse, miniCartData, localCacheModel)?.let { item ->
//                    add(item)
//                }
//
//                addSwitcherUiModel(layoutResponse, localCacheModel, isLoggedIn)
//            }
    }
}

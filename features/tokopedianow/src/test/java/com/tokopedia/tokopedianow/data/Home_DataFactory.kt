package com.tokopedia.tokopedianow.data

import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.response.Tokonow
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.domain.model.*
import com.tokopedia.tokopedianow.home.presentation.uimodel.*

fun createHomeLayoutList(): List<HomeLayoutResponse> {
    return listOf(
            HomeLayoutResponse(
                    id = "34923",
                    layout = "lego_3_image",
                    header = Header(
                            name = "Lego Banner",
                            serverTimeUnix = 0
                    ),
            ),
            HomeLayoutResponse(
                    id = "11111",
                    layout = "category_tokonow",
                    header = Header(
                            name = "Category Tokonow",
                            serverTimeUnix = 0
                    )
            ),
            HomeLayoutResponse(
                    id = "2222",
                    layout = "banner_carousel_v2",
                    header = Header(
                            name = "Banner Tokonow",
                            serverTimeUnix = 0
                    )
            )
    )
}

fun createHomeLayoutListForBannerOnly(): List<HomeLayoutResponse> {
    return listOf(
            HomeLayoutResponse(
                    id = "2222",
                    layout = "banner_carousel_v2",
                    header = Header(
                            name = "Banner Tokonow",
                            serverTimeUnix = 0
                    )
            )
    )
}

fun createHomeLayoutData(): HomeLayoutResponse {
    return HomeLayoutResponse(
            id = "2222",
            layout = "banner_carousel_v2",
            header = Header(
                    name = "Banner Tokonow",
                    serverTimeUnix = 0
            )
    )
}

fun createLoadingState(): HomeLayoutListUiModel {
    val mutableList = mutableListOf<HomeLayoutItemUiModel>()
    val loadingStateUiModel = HomeLoadingStateUiModel(id = HomeStaticLayoutId.LOADING_STATE)
    mutableList.add(HomeLayoutItemUiModel(loadingStateUiModel, HomeLayoutItemState.LOADED))
    return HomeLayoutListUiModel(
            result = mutableList,
            state = HomeLayoutState.LOADING
    )
}

fun createEmptyState(id: String): HomeLayoutListUiModel {
    val mutableList = mutableListOf<HomeLayoutItemUiModel>()
    val chooseAddressUiModel = HomeChooseAddressWidgetUiModel(id = HomeStaticLayoutId.CHOOSE_ADDRESS_WIDGET_ID)
    val emptyStateUiModel = HomeEmptyStateUiModel(id = id)
    mutableList.add(HomeLayoutItemUiModel(chooseAddressUiModel, HomeLayoutItemState.LOADED))
    mutableList.add(HomeLayoutItemUiModel(emptyStateUiModel, HomeLayoutItemState.LOADED))
    return HomeLayoutListUiModel(
            mutableList,
            state = HomeLayoutState.HIDE
    )
}

fun createTicker(): TickerResponse {
    return  TickerResponse(
            ticker = Tickers(
                    tickerList = listOf(
                            Ticker(
                                    id = "10",
                                    title = "Welcome to Tokonow",
                                    message = "Tokonow is one of the best feature",
                                    color = "#FFF"
                            )

                    )
            )
    )
}

fun createKeywordSearch(): KeywordSearchData {
    return KeywordSearchData(
            searchData = SearchPlaceholder(
                    data = Data(
                            keyword = "Hello World",
                            placeholder = "Search tomato"
                    )
            )
    )
}

fun createChooseAddress(): GetStateChosenAddressQglResponse {
    return GetStateChosenAddressQglResponse(
            response = GetStateChosenAddressResponse(
                    tokonow = Tokonow(
                            shopId = 121231,
                            warehouseId = 21313
                    )
            )
    )
}

fun createMiniCartSimplifier(): MiniCartSimplifiedData {
    return MiniCartSimplifiedData(
            miniCartWidgetData = MiniCartWidgetData(
                    totalProductCount = 1,
                    totalProductPrice = 100
            ),
            miniCartItems = listOf(
                    MiniCartItem(
                            isError = false,
                            cartId = "123",
                            productId = "125",
                            productParentId = "126",
                            quantity = 12,
                            notes = "Hai"
                    )
            ),
            isShowMiniCartWidget = true
    )
}

fun createCategoryGridListFirstFetch(): CategoryListResponse {
    return CategoryListResponse(
            header = com.tokopedia.abstraction.common.data.model.response.Header(),
            data = listOf(
            CategoryResponse(
                    id = "3",
                    name = "Category 3",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "5",
                    childList = listOf()
            )
    ))
}

fun createCategoryGridListSecondFetch(): CategoryListResponse {
    return CategoryListResponse(
            header = com.tokopedia.abstraction.common.data.model.response.Header(),
            data = listOf(
                    CategoryResponse(
                            id = "1",
                            name = "Category 1",
                            url = "tokopedia://",
                            appLinks = "tokoepdia://",
                            imageUrl = "tokopedia://",
                            parentId = "2",
                            childList = listOf()
                    )
            ))
}

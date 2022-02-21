package com.tokopedia.tokopedianow.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.response.Tokonow
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryListResponse
import com.tokopedia.tokopedianow.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.model.*
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.domain.model.*
import com.tokopedia.tokopedianow.home.presentation.uimodel.*
import com.tokopedia.tokopedianow.repurchase.presentation.fragment.TokoNowRepurchaseFragment
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ANNOUNCEMENT
import com.tokopedia.unifycomponents.ticker.TickerData

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
                    ),
                    token = "==aff1ed" // Dummy token
            )
    )
}

fun createHomeLayoutListForQuestOnly(): List<HomeLayoutResponse> {
    return listOf(
        HomeLayoutResponse(
            id = "55678",
            layout = "tokonow_main_quest",
            header = Header(
                name = "Main Quest",
                serverTimeUnix = 0
            ),
            token = "==aff1ed" // Dummy token
        )
    )
}

fun createQuestWidgetListEmpty(code: String, reason: String = ""): GetQuestListResponse {
    return GetQuestListResponse(
        questWidgetList = QuestListResponse(
            questWidgetList = listOf(),
            resultStatus = ResultStatus(
                code = code,
                reason = reason
            )
        )
    )
}

fun createQuestWidgetList(code: String, reason: String = ""): GetQuestListResponse {
    return GetQuestListResponse(
        questWidgetList = QuestListResponse(
            questWidgetList = listOf(
                QuestList(
                    id = "1233",
                    title = "dummy title",
                    description = "dummy desc",
                    config = "{}",
                    questUser = QuestUser(
                        id = "1111",
                        status = "Idle"
                    ),
                    task = listOf()
                )
            ),
            resultStatus = ResultStatus(
                code = code,
                reason = reason
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
            ),
            token = "==aff1ed" // Dummy token
    )
}

fun createLoadingState(): HomeLayoutListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val loadingStateUiModel = HomeLoadingStateUiModel(id = HomeStaticLayoutId.LOADING_STATE)
    mutableList.add(loadingStateUiModel)
    return HomeLayoutListUiModel(
            items = mutableList,
            state = TokoNowLayoutState.LOADING
    )
}

fun createEmptyState(id: String, serviceType: String): HomeLayoutListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val chooseAddressUiModel = TokoNowChooseAddressWidgetUiModel(id = HomeStaticLayoutId.CHOOSE_ADDRESS_WIDGET_ID)
    val emptyStateUiModel = TokoNowEmptyStateOocUiModel(id = id, hostSource = TokoNowRepurchaseFragment.SOURCE, serviceType = serviceType)
    mutableList.add(chooseAddressUiModel)
    mutableList.add(emptyStateUiModel)
    return HomeLayoutListUiModel(
            mutableList,
            state = TokoNowLayoutState.HIDE
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
                                    color = "#FFF",
                                    layout = "default"
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

fun createDynamicLegoBannerDataModel(
    id: String,
    groupId: String,
    headerName: String,
    headerServerTimeUnix: Long = 0
): DynamicLegoBannerDataModel {
    val channelHeader = ChannelHeader(name = headerName, serverTimeUnix = headerServerTimeUnix)
    val channelConfig = ChannelConfig(layout = "lego_3_image")
    val channelModel = ChannelModel(
        id = id,
        groupId = groupId,
        layout = "lego_3_image",
        channelHeader = channelHeader,
        channelConfig = channelConfig
    )
    return DynamicLegoBannerDataModel(channelModel = channelModel)
}

fun createSliderBannerDataModel(
    id: String,
    groupId: String,
    headerName: String,
    headerServerTimeUnix: Long = 0
): BannerDataModel {
    val channelHeader = ChannelHeader(name = headerName, serverTimeUnix = headerServerTimeUnix)
    val channelConfig = ChannelConfig(layout = "banner_carousel_v2")
    val channelModel = ChannelModel(
        id = id,
        groupId = groupId,
        layout = "banner_carousel_v2",
        channelHeader = channelHeader,
        channelConfig = channelConfig
    )
    return BannerDataModel(channelModel = channelModel)
}

fun createCategoryGridDataModel(
    id: String,
    title: String,
    categoryList: List<TokoNowCategoryItemUiModel>?,
    @TokoNowLayoutState state: Int
): TokoNowCategoryGridUiModel {
    return TokoNowCategoryGridUiModel(id = id, title =  title, categoryList = categoryList, state = state)
}

fun createHomeTickerDataModel(tickers: List<TickerData> = listOf(createTickerData())): HomeTickerUiModel {
    return HomeTickerUiModel(id = "1", tickers = tickers)
}

fun createTickerData(
    title: String = "Welcome to Tokonow",
    description: String = "Tokonow is one of the best feature",
    type: Int = TYPE_ANNOUNCEMENT
): TickerData {
    return TickerData(title = title, description = description, type = type)
}

fun createHomeProductCardUiModel(
    productId: String = "",
    shopId: String = "",
    quantity: Int = 0,
    parentId: String = "",
    product: ProductCardModel = ProductCardModel(),
    @TokoNowLayoutType type: String = TokoNowLayoutType.REPURCHASE_PRODUCT
): TokoNowProductCardUiModel {
    return TokoNowProductCardUiModel(productId, shopId, quantity, parentId, product, type)
}

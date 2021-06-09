package com.tokopedia.tokomart.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.*
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import com.tokopedia.tokomart.categorylist.domain.model.CategoryResponse
import com.tokopedia.tokomart.home.domain.model.*
import com.tokopedia.tokomart.home.presentation.uimodel.*
import com.tokopedia.unifycomponents.ticker.TickerData
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

fun createHomeLayoutList(): List<HomeLayoutResponse> {
    return listOf(
            HomeLayoutResponse(
                    id = "34923",
                    layout = "category_tokonow",
                    header = Header(
                            name = "Category Tokonow",
                            serverTimeUnix = 0
                    ),
            )
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

fun createCategoryListData(): List<CategoryResponse> {
    return listOf(
            CategoryResponse(
                    id = "1",
                    name = "Category 1",
                    url = "tokopedia://",
                    appLinks = "tokoepdia://",
                    imageUrl = "tokopedia://",
                    parentId = "2",
                    childList = listOf()
            )
    )
}

fun createHomeLayoutListWithCategory(): HomeLayoutListUiModel {
    return HomeLayoutListUiModel(
            result = listOf<Visitable<*>>(
                    HomeChooseAddressWidgetUiModel(id = "0", isRefresh = false, isMyShop = false),
                    HomeTickerUiModel(id = "1", tickers = listOf(TickerData(description = "Tokonow is one of the best feature", type = 0))),
                    HomeCategoryGridUiModel(id = "34923", title = "Category Tokonow", categoryList = listOf(HomeCategoryItemUiModel(id = "1", title = "Category 1", imageUrl = "tokopedia://", appLink = "tokoepdia://"))),
                    BannerDataModel(channelModel = ChannelModel(id = "34922", groupId = "", type = "", style = ChannelStyle.ChannelHome, verticalPosition = 0, contextualInfo = 0, widgetParam = "", pageName = "",
                            channelHeader = ChannelHeader(id = "", name = "Banner Tokonow", subtitle = "", expiredTime = "", serverTimeUnix = 0, applink = "", url = "", backColor = "", backImage = "", textColor = ""),
                            channelBanner = ChannelBanner(id = "", title = "", description = "", backColor = "", url = "", applink = "", textColor = "", imageUrl = "", attribution = "",
                                    cta = ChannelCtaData(type = "", mode = "", text = "", couponCode = ""), gradientColor = arrayListOf("")),
                            channelConfig = ChannelConfig(layout = "banner_carousel_v2", showPromoBadge = false, hasCloseButton = false, serverTimeOffset = 0, createdTimeMillis = "", isAutoRefreshAfterExpired = false, enableTimeDiffMoreThan24h = false, dividerType = 0),
                            trackingAttributionModel = TrackingAttributionModel(galaxyAttribution = "", persona = "", brandId = "", categoryPersona = "", categoryId = "", persoType = "", campaignCode = "", homeAttribution = "", campaignId = "", promoName = ""),
                            channelGrids = listOf(), name = "", layout = "banner_carousel_v2"))),
            isInitialLoad = false,
            isHeaderBackgroundShowed = true
    )
}

fun createHomeLayoutListwithHome(): List<HomeLayoutResponse> {
    return listOf(
            HomeLayoutResponse(
                    id = "34923",
                    layout = "category_tokonow",
                    header = Header(
                            name = "Category Tokonow",
                            serverTimeUnix = 0
                    ),
            ),
            HomeLayoutResponse(
                    id = "34922",
                    layout = "banner_carousel_v2",
                    header = Header(
                            name = "Banner Tokonow",
                            serverTimeUnix = 0
                    ),
            )
    )
}

fun createHomeLayoutListwithBanner(): HomeLayoutResponse {
    return HomeLayoutResponse(
            id = "34922",
            layout = "banner_carousel_v2",
            header = Header(
                    name = "Banner Tokonow",
                    serverTimeUnix = 0
            )
    )
}

fun createHomeLayoutBannerModelEmptyID(): List<Visitable<*>> {
    return listOf<Visitable<*>>(
                    BannerDataModel(channelModel = ChannelModel(id = "", groupId = "", type = "", style = ChannelStyle.ChannelHome, verticalPosition = 0, contextualInfo = 0, widgetParam = "", pageName = "",
                            channelHeader = ChannelHeader(id = "", name = "Banner Tokonow", subtitle = "", expiredTime = "", serverTimeUnix = 0, applink = "", url = "", backColor = "", backImage = "", textColor = ""),
                            channelBanner = ChannelBanner(id = "", title = "", description = "", backColor = "", url = "", applink = "", textColor = "", imageUrl = "", attribution = "",
                                    cta = ChannelCtaData(type = "", mode = "", text = "", couponCode = ""), gradientColor = arrayListOf()),
                            channelConfig = ChannelConfig(layout = "banner_carousel_v2", showPromoBadge = false, hasCloseButton = false, serverTimeOffset = 0, createdTimeMillis = "", isAutoRefreshAfterExpired = false, enableTimeDiffMoreThan24h = false, dividerType = 0),
                            trackingAttributionModel = TrackingAttributionModel(galaxyAttribution = "", persona = "", brandId = "", categoryPersona = "", categoryId = "", persoType = "", campaignCode = "", homeAttribution = "", campaignId = "", promoName = ""),
                            channelGrids = listOf(), name = "", layout = "banner_carousel_v2"))
    )
}

fun createHomeLayoutListWithBannerEmptyId(): HomeLayoutListUiModel {
    return HomeLayoutListUiModel(
            result = listOf<Visitable<*>>(
                    BannerDataModel(channelModel = ChannelModel(id = "34922", groupId = "", type = "", style = ChannelStyle.ChannelHome, verticalPosition = 0, contextualInfo = 0, widgetParam = "", pageName = "",
                            channelHeader = ChannelHeader(id = "", name = "Banner Tokonow", subtitle = "", expiredTime = "", serverTimeUnix = 0, applink = "", url = "", backColor = "", backImage = "", textColor = ""),
                            channelBanner = ChannelBanner(id = "", title = "", description = "", backColor = "", url = "", applink = "", textColor = "", imageUrl = "", attribution = "",
                                    cta = ChannelCtaData(type = "", mode = "", text = "", couponCode = ""), gradientColor = arrayListOf("")),
                            channelConfig = ChannelConfig(layout = "banner_carousel_v2", showPromoBadge = false, hasCloseButton = false, serverTimeOffset = 0, createdTimeMillis = "", isAutoRefreshAfterExpired = false, enableTimeDiffMoreThan24h = false, dividerType = 0),
                            trackingAttributionModel = TrackingAttributionModel(galaxyAttribution = "", persona = "", brandId = "", categoryPersona = "", categoryId = "", persoType = "", campaignCode = "", homeAttribution = "", campaignId = "", promoName = ""),
                            channelGrids = listOf(), name = "", layout = "banner_carousel_v2"))),
            isInitialLoad = false
    )
}



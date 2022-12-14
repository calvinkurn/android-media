package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class TokoNowHomeViewModelTestRealTimeRecommendation : TokoNowHomeViewModelTestFixture() {

    @Test
    fun `given rtr wiget param page name and interaction TRUE when addProductToCart should get real time recommendation`() {
        val productId = "2"
        val channelId = "1001"
        val rtrEnabled = true
        val rtrPageName = "rtr_default"
        val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_default"

        val rtrItemList = listOf(
            RecommendationItem(
                productId = 5,
                name = "Tahu Bulat"
            )
        )

        val rtrWidgetListResponse = listOf(
            RecommendationWidget(recommendationItemList = rtrItemList),
            RecommendationWidget(recommendationItemList = emptyList())
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(
                        id = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        categoryBreadcrumbs = "Sayur"
                    )
                ),
                widgetParam = rtrWidgetParam
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecommendation_thenReturn(rtrWidgetListResponse)
        onAddToCart_thenReturn(AddToCartDataModel())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, productId, 1, "1", TokoNowLayoutType.PRODUCT_RECOM)

        val recomItemList = listOf(
            RecommendationItem(
                productId = 2,
                categoryBreadcrumbs = "Sayur",
                imageUrl = "https://tokopedia.com/image.jpg",
                isRecomProductShowVariantAndCart = true,
                price = "0",
                position = 1,
                quantity = 1
            )
        )

        val recomWidget = RecommendationWidget(
            title = "Lagi Diskon",
            recommendationItemList = recomItemList
        )

        val rtrWidget = RecommendationWidget(
            title = "",
            recommendationItemList = rtrItemList
        )

        val realTimeRecom = HomeRealTimeRecomUiModel(
            channelId = channelId,
            headerName = "Lagi Diskon",
            parentProductId = productId,
            productImageUrl = "https://tokopedia.com/image.jpg",
            category = "Sayur",
            widget = rtrWidget,
            enabled = rtrEnabled,
            pageName = rtrPageName,
            widgetState = RealTimeRecomWidgetState.READY,
            carouselState = RecommendationCarouselData.STATE_READY,
            type = TokoNowLayoutType.PRODUCT_RECOM
        )

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = realTimeRecom
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            homeRecomUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRealTimeRecommendationCalled(
            pageName = rtrPageName,
            productId = listOf(productId)
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given add product to cart when addProductToCart other product should map real time recom widget state REFRESH`() {
        val channelId = "1001"
        val rtrEnabled = true
        val rtrPageName = "rtr_default"
        val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_default"

        val rtrItemList = listOf(
            RecommendationItem(
                productId = 5,
                name = "Tahu Bulat",
                quantity = 2
            )
        )

        val rtrWidgetListResponse = listOf(
            RecommendationWidget(recommendationItemList = rtrItemList),
            RecommendationWidget(recommendationItemList = emptyList())
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(
                        id = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        categoryBreadcrumbs = "Sayur"
                    ),
                    Grid(
                        id = "5",
                        imageUrl = "https://tokopedia.com/image_5.jpg",
                        categoryBreadcrumbs = "Daging"
                    )
                ),
                widgetParam = rtrWidgetParam
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecommendation_thenReturn(rtrWidgetListResponse)
        onAddToCart_thenReturn(AddToCartDataModel())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, "2", 1, "1", TokoNowLayoutType.PRODUCT_RECOM)
        viewModel.addProductToCart(channelId, "5", 2, "1", TokoNowLayoutType.PRODUCT_RECOM)

        val recomItemList = listOf(
            RecommendationItem(
                productId = 2,
                categoryBreadcrumbs = "Sayur",
                imageUrl = "https://tokopedia.com/image.jpg",
                isRecomProductShowVariantAndCart = true,
                price = "0",
                position = 1,
                quantity = 1
            ),
            RecommendationItem(
                productId = 5,
                categoryBreadcrumbs = "Daging",
                imageUrl = "https://tokopedia.com/image_5.jpg",
                isRecomProductShowVariantAndCart = true,
                price = "0",
                position = 2,
                quantity = 2
            )
        )

        val recomWidget = RecommendationWidget(
            title = "Lagi Diskon",
            recommendationItemList = recomItemList
        )

        val rtrWidget = RecommendationWidget(
            title = "",
            recommendationItemList = rtrItemList
        )

        val realTimeRecom = HomeRealTimeRecomUiModel(
            channelId = channelId,
            headerName = "Lagi Diskon",
            parentProductId = "5",
            productImageUrl = "https://tokopedia.com/image.jpg",
            category = "Sayur",
            widget = rtrWidget,
            enabled = rtrEnabled,
            pageName = rtrPageName,
            widgetState = RealTimeRecomWidgetState.REFRESH,
            carouselState = RecommendationCarouselData.STATE_READY,
            type = TokoNowLayoutType.PRODUCT_RECOM
        )

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = realTimeRecom
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            homeRecomUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRealTimeRecommendationCalled(
            pageName = rtrPageName,
            productId = listOf("2")
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given empty real time recom response when refreshRealTimeRecommendation should map latest real time recom data`() {
        val channelId = "1001"
        val rtrEnabled = true
        val rtrPageName = "rtr_default"
        val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_default"

        val rtrItemList = listOf(
            RecommendationItem(
                productId = 5,
                name = "Tahu Bulat"
            )
        )

        val rtrWidgetListResponse = listOf(
            RecommendationWidget(recommendationItemList = rtrItemList),
            RecommendationWidget(recommendationItemList = emptyList())
        )

        val emptyRtrWidgetListResponse = listOf(
            RecommendationWidget(recommendationItemList = emptyList()),
            RecommendationWidget(recommendationItemList = emptyList())
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(
                        id = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        categoryBreadcrumbs = "Sayur"
                    ),
                    Grid(
                        id = "5",
                        imageUrl = "https://tokopedia.com/image_5.jpg",
                        categoryBreadcrumbs = "Daging"
                    )
                ),
                widgetParam = rtrWidgetParam
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecommendation_thenReturn(rtrWidgetListResponse)
        onAddToCart_thenReturn(AddToCartDataModel())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, "2", 1, "1", TokoNowLayoutType.PRODUCT_RECOM)

        onGetRecommendation_thenReturn(emptyRtrWidgetListResponse)

        viewModel.refreshRealTimeRecommendation(channelId, "5", TokoNowLayoutType.PRODUCT_RECOM)

        val recomItemList = listOf(
            RecommendationItem(
                productId = 2,
                categoryBreadcrumbs = "Sayur",
                imageUrl = "https://tokopedia.com/image.jpg",
                isRecomProductShowVariantAndCart = true,
                price = "0",
                position = 1,
                quantity = 1
            ),
            RecommendationItem(
                productId = 5,
                categoryBreadcrumbs = "Daging",
                imageUrl = "https://tokopedia.com/image_5.jpg",
                isRecomProductShowVariantAndCart = true,
                price = "0",
                position = 2,
                quantity = 0
            )
        )

        val recomWidget = RecommendationWidget(
            title = "Lagi Diskon",
            recommendationItemList = recomItemList
        )

        val rtrWidget = RecommendationWidget(
            title = "",
            recommendationItemList = rtrItemList
        )

        val latestRealTimeRecom = HomeRealTimeRecomUiModel(
            channelId = channelId,
            headerName = "Lagi Diskon",
            parentProductId = "2",
            productImageUrl = "https://tokopedia.com/image.jpg",
            category = "Sayur",
            widget = rtrWidget,
            enabled = rtrEnabled,
            pageName = rtrPageName,
            widgetState = RealTimeRecomWidgetState.READY,
            carouselState = RecommendationCarouselData.STATE_READY,
            type = TokoNowLayoutType.PRODUCT_RECOM
        )

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = latestRealTimeRecom
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            homeRecomUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRealTimeRecommendationCalled(
            pageName = rtrPageName,
            productId = listOf("5")
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given rtr wiget param page name and interaction FALSE when addProductToCart should NOT get real time recommendation`() {
        val productId = "2"
        val channelId = "1001"
        val rtrEnabled = false
        val rtrPageName = "rtr_default"
        val rtrWidgetParam = "?rtr_interaction=false&rtr_pagename=rtr_default"

        val rtrItemList = listOf(
            RecommendationItem(
                productId = 5,
                name = "Tahu Bulat"
            )
        )

        val rtrWidgetListResponse = listOf(
            RecommendationWidget(recommendationItemList = rtrItemList),
            RecommendationWidget(recommendationItemList = emptyList())
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(
                        id = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        categoryBreadcrumbs = "Sayur"
                    )
                ),
                widgetParam = rtrWidgetParam
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecommendation_thenReturn(rtrWidgetListResponse)
        onAddToCart_thenReturn(AddToCartDataModel())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, productId, 1, "1", TokoNowLayoutType.PRODUCT_RECOM)

        val recomItemList = listOf(
            RecommendationItem(
                productId = 2,
                categoryBreadcrumbs = "Sayur",
                imageUrl = "https://tokopedia.com/image.jpg",
                isRecomProductShowVariantAndCart = true,
                price = "0",
                position = 1,
                quantity = 1
            )
        )

        val recomWidget = RecommendationWidget(
            title = "Lagi Diskon",
            recommendationItemList = recomItemList
        )

        val realTimeRecom = HomeRealTimeRecomUiModel(
            channelId = channelId,
            headerName = "Lagi Diskon",
            parentProductId = "",
            productImageUrl = "",
            category = "",
            widget = null,
            enabled = rtrEnabled,
            pageName = rtrPageName,
            carouselState = RecommendationCarouselData.STATE_LOADING,
            type = TokoNowLayoutType.PRODUCT_RECOM
        )

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = realTimeRecom
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            homeRecomUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRealTimeRecommendationNotCalled(
            pageName = rtrPageName,
            productId = listOf(productId)
        )

        viewModel.atcQuantity
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given get real time recommendation error when addProductToCart should do nothing`() {
        val productId = "2"
        val channelId = "1001"
        val rtrPageName = "rtr_default"
        val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_default"

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(
                        id = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        categoryBreadcrumbs = "Sayur"
                    )
                ),
                widgetParam = rtrWidgetParam
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecommendation_thenReturn(NullPointerException())
        onAddToCart_thenReturn(AddToCartDataModel())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, productId, 1, "1", TokoNowLayoutType.PRODUCT_RECOM)

        verifyGetRealTimeRecommendationCalled(
            pageName = rtrPageName,
            productId = listOf(productId)
        )
    }

    @Test
    fun `given real time recommendation when removeRealTimeRecommendation should map real time recom widget null`() {
        val rtrWidget = null
        val productId = "2"
        val channelId = "1001"
        val rtrEnabled = true
        val rtrPageName = "rtr_default"
        val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_default"

        val rtrItemList = listOf(
            RecommendationItem(
                productId = 5,
                name = "Tahu Bulat"
            )
        )

        val rtrWidgetListResponse = listOf(
            RecommendationWidget(recommendationItemList = rtrItemList),
            RecommendationWidget(recommendationItemList = emptyList())
        )

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = channelId,
                layout = "top_carousel_tokonow",
                header = Header(
                    name = "Lagi Diskon",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(
                        id = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        categoryBreadcrumbs = "Sayur"
                    )
                ),
                widgetParam = rtrWidgetParam
            )
        )

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRecommendation_thenReturn(rtrWidgetListResponse)
        onAddToCart_thenReturn(AddToCartDataModel())

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart(channelId, productId, 1, "1", TokoNowLayoutType.PRODUCT_RECOM)
        viewModel.removeRealTimeRecommendation(channelId, TokoNowLayoutType.PRODUCT_RECOM)

        val recomItemList = listOf(
            RecommendationItem(
                productId = 2,
                categoryBreadcrumbs = "Sayur",
                imageUrl = "https://tokopedia.com/image.jpg",
                isRecomProductShowVariantAndCart = true,
                price = "0",
                position = 1,
                quantity = 1
            )
        )

        val recomWidget = RecommendationWidget(
            title = "Lagi Diskon",
            recommendationItemList = recomItemList
        )

        val realTimeRecom = HomeRealTimeRecomUiModel(
            channelId = channelId,
            headerName = "Lagi Diskon",
            parentProductId = productId,
            productImageUrl = "https://tokopedia.com/image.jpg",
            category = "Sayur",
            widget = rtrWidget,
            enabled = rtrEnabled,
            pageName = rtrPageName,
            widgetState = RealTimeRecomWidgetState.READY,
            carouselState = RecommendationCarouselData.STATE_READY,
            type = TokoNowLayoutType.PRODUCT_RECOM
        )

        val homeRecomUiModel = HomeProductRecomUiModel(
            id = "1001",
            recomWidget = recomWidget,
            realTimeRecom = realTimeRecom
        )

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(id = "0"),
            homeRecomUiModel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRealTimeRecommendationCalled(
            pageName = rtrPageName,
            productId = listOf(productId)
        )

        viewModel.homeLayoutList
            .verifySuccessEquals(expectedResult)
    }

    @Test
    fun `given homeLayoutItemList throws exception when removeRealTimeRecommendation should do nothing`() {
        onGetHomeLayoutItemList_returnNull()

        viewModel.removeRealTimeRecommendation("5", TokoNowLayoutType.PRODUCT_RECOM)

        viewModel.homeLayoutList
            .verifyValueEquals(null)
    }

    @Test
    fun `given getRealTimeRecom null when refresh real time recom should NOT call get recommendation use case`() {
        viewModel.refreshRealTimeRecommendation("1", "2", TokoNowLayoutType.PRODUCT_RECOM)
        
        verifyGetRealTimeRecommendationNotCalled(pageName = "rtr_tokonow", listOf("2"))
    }

    @Test
    fun `given empty channelId when addProductToCart should NOT call get recommendation use case`() {
        viewModel.addProductToCart(
            productId = "2",
            quantity = 1,
            shopId = "5",
            type = TokoNowLayoutType.PRODUCT_RECOM
        )

        verifyGetRealTimeRecommendationNotCalled(pageName = "rtr_tokonow", listOf("2"))
    }
}

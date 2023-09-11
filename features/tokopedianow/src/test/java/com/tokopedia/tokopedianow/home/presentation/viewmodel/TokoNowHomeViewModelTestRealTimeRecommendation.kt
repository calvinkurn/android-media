package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.Shop
import com.tokopedia.tokopedianow.home.mapper.HomeHeaderMapper.createHomeHeaderUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTestRealTimeRecommendation : TokoNowHomeViewModelTestFixture() {

    companion object {
        private const val CHANGE_QUANTITY_DELAY = 1000L
    }

    @Test
    fun `given rtr wiget param page name and interaction TRUE when addProductToCart should get real time recommendation`() {
        runTest {
            val productId = "2"
            val channelId = "1001"
            val rtrEnabled = true
            val rtrPageName = "rtr_default"
            val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_default"

            val rtrItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    shopId = 5,
                    name = "Tahu Bulat",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    appUrl = "tokopedia://product/detail/1"
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
                        id = "5",
                        name = "Lagi Diskon",
                        applink = "tokopedia://now",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "2",
                            shop = Shop(shopId = "5"),
                            parentProductId = "0",
                            imageUrl = "https://tokopedia.com/image.jpg",
                            categoryBreadcrumbs = "Bahan Masak/Sayur"
                        )
                    ),
                    widgetParam = rtrWidgetParam
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(rtrWidgetListResponse)
            onAddToCart_thenReturn(AddToCartDataModel())

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf()
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, productId, 1, "1", 0, false, TokoNowLayoutType.PRODUCT_RECOM)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    shopType = "pm",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        isVariant = false,
                        price = "0",
                        orderQuantity = 1,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    ),
                    parentId = "0"
                )
            )

            val rtrProductList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    appLink = "tokopedia://product/detail/1",
                    headerName = "Lagi Diskon",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "5",
                        name = "Tahu Bulat",
                        usePreDraw = true,
                        needToShowQuantityEditor = true,
                        needToChangeMaxLinesName = true
                    ),
                    parentId = "0"
                )
            )

            val realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelId,
                headerName = "Lagi Diskon",
                parentProductId = productId,
                productImageUrl = "https://tokopedia.com/image.jpg",
                category = "Sayur",
                productList = rtrProductList,
                enabled = rtrEnabled,
                pageName = rtrPageName,
                widgetState = RealTimeRecomWidgetState.READY,
                carouselState = TokoNowProductRecommendationState.LOADED,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )

            val seeMoreUiModel = ProductCardCompactCarouselSeeMoreUiModel(
                id = "5",
                headerName = "Lagi Diskon",
                appLink = "tokopedia://now"
            )

            val headerUiModel = TokoNowDynamicHeaderUiModel(
                title = "Lagi Diskon",
                subTitle = "",
                ctaText = "",
                ctaTextLink = "tokopedia://now",
                expiredTime = "",
                serverTimeOffset = 0,
                backColor = ""
            )

            val homeRecomUiModel = HomeProductRecomUiModel(
                id = "1001",
                title = "Lagi Diskon",
                productList = productList,
                seeMoreModel = seeMoreUiModel,
                headerModel = headerUiModel,
                realTimeRecom = realTimeRecom
            )

            val homeLayoutItems = listOf(
                createHomeHeaderUiModel(),
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
    }

    @Test
    fun `given add product to cart when addProductToCart other product should map real time recom widget state REFRESH`() {
        runTest {
            val channelId = "1001"
            val rtrEnabled = true
            val rtrPageName = "rtr_default"
            val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_default"

            val rtrItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    parentID = 2,
                    shopId = 2,
                    name = "Tahu Bulat",
                    quantity = 2,
                    categoryBreadcrumbs = "Bahan Masak/Sayur"
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
                            parentProductId = "3",
                            shop = Shop(shopId = "5"),
                            price = "2000",
                            imageUrl = "https://tokopedia.com/image.jpg",
                            categoryBreadcrumbs = "Bahan Masak/Sayur"
                        ),
                        Grid(
                            id = "5",
                            parentProductId = "7",
                            shop = Shop(shopId = "2"),
                            price = "3000",
                            imageUrl = "https://tokopedia.com/image_5.jpg",
                            categoryBreadcrumbs = "Bahan Masak/Daging"
                        )
                    ),
                    widgetParam = rtrWidgetParam
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(rtrWidgetListResponse)
            onAddToCart_thenReturn(AddToCartDataModel())

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf()
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, "2", 1, "1", 0, false, TokoNowLayoutType.PRODUCT_RECOM)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)
            viewModel.onCartQuantityChanged(channelId, "5", 2, "1", 0, false, TokoNowLayoutType.PRODUCT_RECOM)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    parentId = "3",
                    shopId = "5",
                    shopType = "pm",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        price = "2000",
                        orderQuantity = 1,
                        isVariant = true,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    )
                ),
                ProductCardCompactCarouselItemUiModel(
                    parentId = "7",
                    shopId = "2",
                    shopType = "pm",
                    categoryBreadcrumbs = "Bahan Masak/Daging",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "5",
                        imageUrl = "https://tokopedia.com/image_5.jpg",
                        price = "3000",
                        orderQuantity = 2,
                        isVariant = true,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    )
                )
            )

            val rtrProductList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    parentId = "2",
                    shopId = "2",
                    headerName = "Lagi Diskon",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "5",
                        name = "Tahu Bulat",
                        orderQuantity = 2,
                        isVariant = true,
                        usePreDraw = true,
                        needToShowQuantityEditor = true,
                        needToChangeMaxLinesName = true
                    )
                )
            )

            val realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelId,
                headerName = "Lagi Diskon",
                parentProductId = "5",
                productImageUrl = "https://tokopedia.com/image.jpg",
                category = "Sayur",
                productList = rtrProductList,
                enabled = rtrEnabled,
                pageName = rtrPageName,
                widgetState = RealTimeRecomWidgetState.REFRESH,
                carouselState = TokoNowProductRecommendationState.LOADED,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )

            val seeMoreUiModel = ProductCardCompactCarouselSeeMoreUiModel(
                id = "",
                headerName = "Lagi Diskon",
                appLink = ""
            )

            val headerUiModel = TokoNowDynamicHeaderUiModel(
                title = "Lagi Diskon",
                subTitle = "",
                ctaText = "",
                ctaTextLink = "",
                expiredTime = "",
                serverTimeOffset = 0,
                backColor = ""
            )

            val homeRecomUiModel = HomeProductRecomUiModel(
                id = "1001",
                title = "Lagi Diskon",
                productList = productList,
                seeMoreModel = seeMoreUiModel,
                headerModel = headerUiModel,
                realTimeRecom = realTimeRecom
            )

            val homeLayoutItems = listOf(
                createHomeHeaderUiModel(),
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
    }

    @Test
    fun `given empty real time recom response when refreshRealTimeRecommendation should map latest real time recom data`() {
        runTest {
            val productId = "2"
            val channelId = "1001"
            val rtrEnabled = true
            val rtrPageName = "rtr_default"
            val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_default"

            val rtrItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    shopId = 5,
                    name = "Tahu Bulat",
                    categoryBreadcrumbs = "Bahan Masak/Sayur"
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
                        id = "5",
                        name = "Lagi Diskon",
                        applink = "tokopedia://now",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "2",
                            shop = Shop(shopId = "5"),
                            parentProductId = "0",
                            imageUrl = "https://tokopedia.com/image.jpg",
                            categoryBreadcrumbs = "Bahan Masak/Sayur"
                        )
                    ),
                    widgetParam = rtrWidgetParam
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(rtrWidgetListResponse)
            onAddToCart_thenReturn(AddToCartDataModel())

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf()
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, productId, 1, "1", 0, false, TokoNowLayoutType.PRODUCT_RECOM)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            onGetRecommendation_thenReturn(emptyRtrWidgetListResponse)

            viewModel.refreshRealTimeRecommendation(channelId, productId, TokoNowLayoutType.PRODUCT_RECOM)

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    shopType = "pm",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        isVariant = false,
                        price = "0",
                        orderQuantity = 1,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    ),
                    parentId = "0"
                )
            )

            val rtrProductList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    headerName = "Lagi Diskon",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "5",
                        name = "Tahu Bulat",
                        usePreDraw = true,
                        needToShowQuantityEditor = true,
                        needToChangeMaxLinesName = true
                    ),
                    parentId = "0"
                )
            )

            val realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelId,
                headerName = "Lagi Diskon",
                parentProductId = productId,
                productImageUrl = "https://tokopedia.com/image.jpg",
                category = "Sayur",
                productList = rtrProductList,
                enabled = rtrEnabled,
                pageName = rtrPageName,
                widgetState = RealTimeRecomWidgetState.READY,
                carouselState = TokoNowProductRecommendationState.LOADED,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )

            val seeMoreUiModel = ProductCardCompactCarouselSeeMoreUiModel(
                id = "5",
                headerName = "Lagi Diskon",
                appLink = "tokopedia://now"
            )

            val headerUiModel = TokoNowDynamicHeaderUiModel(
                title = "Lagi Diskon",
                subTitle = "",
                ctaText = "",
                ctaTextLink = "tokopedia://now",
                expiredTime = "",
                serverTimeOffset = 0,
                backColor = ""
            )

            val homeRecomUiModel = HomeProductRecomUiModel(
                id = "1001",
                title = "Lagi Diskon",
                productList = productList,
                seeMoreModel = seeMoreUiModel,
                headerModel = headerUiModel,
                realTimeRecom = realTimeRecom
            )

            val homeLayoutItems = listOf(
                createHomeHeaderUiModel(),
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
    }

    @Test
    fun `given rtr wiget param page name and interaction FALSE when addProductToCart should NOT get real time recommendation`() {
        runTest {
            val productId = "2"
            val channelId = "1001"
            val rtrEnabled = false
            val rtrPageName = "rtr_pagename"
            val rtrWidgetParam = "?rtr_interaction=false&rtr_pagename=rtr_pagename"

            val rtrItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    shopId = 5,
                    name = "Tahu Bulat",
                    categoryBreadcrumbs = "Bahan Masak/Sayur"
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
                        id = "5",
                        name = "Lagi Diskon",
                        applink = "tokopedia://now",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "2",
                            shop = Shop(shopId = "5"),
                            parentProductId = "0",
                            imageUrl = "https://tokopedia.com/image.jpg",
                            categoryBreadcrumbs = "Bahan Masak/Sayur"
                        )
                    ),
                    widgetParam = rtrWidgetParam
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(rtrWidgetListResponse)
            onAddToCart_thenReturn(AddToCartDataModel())

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf()
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, productId, 1, "5", 0, false, TokoNowLayoutType.PRODUCT_RECOM)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    shopType = "pm",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        isVariant = false,
                        price = "0",
                        orderQuantity = 1,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    ),
                    parentId = "0"
                )
            )

            val realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelId,
                headerName = "Lagi Diskon",
                parentProductId = "",
                productImageUrl = "",
                category = "",
                productList = emptyList(),
                enabled = rtrEnabled,
                pageName = rtrPageName,
                widgetState = RealTimeRecomWidgetState.IDLE,
                carouselState = TokoNowProductRecommendationState.LOADING,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )

            val seeMoreUiModel = ProductCardCompactCarouselSeeMoreUiModel(
                id = "5",
                headerName = "Lagi Diskon",
                appLink = "tokopedia://now"
            )

            val headerUiModel = TokoNowDynamicHeaderUiModel(
                title = "Lagi Diskon",
                subTitle = "",
                ctaText = "",
                ctaTextLink = "tokopedia://now",
                expiredTime = "",
                serverTimeOffset = 0,
                backColor = ""
            )

            val homeRecomUiModel = HomeProductRecomUiModel(
                id = "1001",
                title = "Lagi Diskon",
                productList = productList,
                seeMoreModel = seeMoreUiModel,
                headerModel = headerUiModel,
                realTimeRecom = realTimeRecom
            )

            val homeLayoutItems = listOf(
                createHomeHeaderUiModel(),
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
    }

    @Test
    fun `given get real time recommendation error when addProductToCart should do nothing`() {
        runTest {
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

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf()
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, productId, 1, "1", 0, false, TokoNowLayoutType.PRODUCT_RECOM)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetRealTimeRecommendationCalled(
                pageName = rtrPageName,
                productId = listOf(productId)
            )
        }
    }

    @Test
    fun `given real time recommendation when removeRealTimeRecommendation should map real time recom product list empty`() {
        runTest {
            val productId = "2"
            val channelId = "1001"
            val rtrEnabled = true
            val rtrPageName = "rtr_pagename"
            val rtrWidgetParam = "?rtr_interaction=true&rtr_pagename=rtr_pagename"

            val rtrItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    shopId = 5,
                    name = "Tahu Bulat",
                    categoryBreadcrumbs = "Bahan Masak/Sayur"
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
                        id = "5",
                        name = "Lagi Diskon",
                        applink = "tokopedia://now",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "2",
                            shop = Shop(shopId = "5"),
                            parentProductId = "0",
                            imageUrl = "https://tokopedia.com/image.jpg",
                            categoryBreadcrumbs = "Bahan Masak/Sayur"
                        )
                    ),
                    widgetParam = rtrWidgetParam
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(rtrWidgetListResponse)
            onAddToCart_thenReturn(AddToCartDataModel())

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf()
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(channelId, productId, 1, "5", 0, false, TokoNowLayoutType.PRODUCT_RECOM)
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            viewModel.removeRealTimeRecommendation(channelId, TokoNowLayoutType.PRODUCT_RECOM)

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    shopType = "pm",
                    categoryBreadcrumbs = "Bahan Masak/Sayur",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "2",
                        imageUrl = "https://tokopedia.com/image.jpg",
                        isVariant = false,
                        price = "0",
                        orderQuantity = 1,
                        usePreDraw = true,
                        needToShowQuantityEditor = true
                    ),
                    parentId = "0"
                )
            )

            val realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelId,
                headerName = "Lagi Diskon",
                parentProductId = "2",
                productImageUrl = "https://tokopedia.com/image.jpg",
                category = "Sayur",
                productList = emptyList(),
                enabled = rtrEnabled,
                pageName = rtrPageName,
                widgetState = RealTimeRecomWidgetState.READY,
                carouselState = TokoNowProductRecommendationState.LOADED,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )

            val seeMoreUiModel = ProductCardCompactCarouselSeeMoreUiModel(
                id = "5",
                headerName = "Lagi Diskon",
                appLink = "tokopedia://now"
            )

            val headerUiModel = TokoNowDynamicHeaderUiModel(
                title = "Lagi Diskon",
                subTitle = "",
                ctaText = "",
                ctaTextLink = "tokopedia://now",
                expiredTime = "",
                serverTimeOffset = 0,
                backColor = ""
            )

            val homeRecomUiModel = HomeProductRecomUiModel(
                id = "1001",
                title = "Lagi Diskon",
                productList = productList,
                seeMoreModel = seeMoreUiModel,
                headerModel = headerUiModel,
                realTimeRecom = realTimeRecom
            )

            val homeLayoutItems = listOf(
                createHomeHeaderUiModel(),
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
        runTest {
            viewModel.onCartQuantityChanged(
                productId = "2",
                quantity = 1,
                shopId = "5",
                stock = 0,
                isVariant = false,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )
            advanceTimeBy(CHANGE_QUANTITY_DELAY)

            verifyGetRealTimeRecommendationNotCalled(pageName = "rtr_tokonow", listOf("2"))
        }
    }
}

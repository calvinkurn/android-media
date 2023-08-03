package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.analytic.HomeAddToCartTracker
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTestCarouselChipsWidget : TokoNowHomeViewModelTestFixture() {

    companion object {
        private const val ADD_TO_CART_DELAY = 700L
        private const val SWITCH_PRODUCT_CAROUSEL_TAB_DELAY = 700L
    }

    @Test
    fun `given chip carousel layout response when getLayoutComponentData should map carousel chips widget to homeLayoutList`() {
        coroutineTestRule.runTest {
            val layout = "chip_carousel"

            val channelId = "1001"
            val productCarouselItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    parentID = 1,
                    shopId = 5,
                    name = "Tahu Bulat",
                    appUrl = "tokopedia://product/detail/1"
                )
            )

            val productCarouselResponse = listOf(
                RecommendationWidget(recommendationItemList = productCarouselItemList),
                RecommendationWidget(recommendationItemList = emptyList())
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = channelId,
                    layout = layout,
                    header = Header(
                        id = "5",
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "1",
                            name = "Sayur-sayuran",
                            param = "?pageName=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pageName=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pageName=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(productCarouselResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            val chipList = listOf(
                TokoNowChipUiModel(
                    id = "1",
                    text = "Sayur-sayuran",
                    param = "?pageName=page_name1",
                    selected = true
                ),
                TokoNowChipUiModel(
                    id = "2",
                    text = "Buah-buahan",
                    param = "?pageName=page_name2",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "3",
                    text = "Bumbu Dapur",
                    param = "?pageName=page_name3",
                    selected = false
                )
            )

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    shopType = "",
                    appLink = "tokopedia://product/detail/1",
                    headerName = "Lagi Diskon",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "5",
                        name = "Tahu Bulat",
                        isVariant = true,
                        price = "",
                        orderQuantity = 0,
                        usePreDraw = true,
                        needToShowQuantityEditor = true,
                        needToChangeMaxLinesName = true
                    ),
                    parentId = "1"
                )
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

            val productCarouselChipsUiModel = HomeProductCarouselChipsUiModel(
                id = "1001",
                header = headerUiModel,
                chipList = chipList,
                carouselItemList = productList,
                state = TokoNowProductRecommendationState.LOADED
            )

            val homeLayoutItems = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                productCarouselChipsUiModel
            )

            val expectedResult = Success(
                HomeLayoutListUiModel(
                    items = homeLayoutItems,
                    state = TokoNowLayoutState.UPDATE
                )
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetCarouselChipsRecomCalled(
                pageName = "page_name1"
            )

            viewModel.homeLayoutList
                .verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `given chip carousel when switchProductCarouselChipTab should update carousel chips widget`() {
        runTest {
            val channelId = "1001"
            val selectedChipId = "3"
            val layout = "chip_carousel"

            val firstProductCarouselItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    parentID = 1,
                    shopId = 5,
                    name = "Tahu Bulat",
                    appUrl = "tokopedia://product/detail/1"
                )
            )

            val firstProductCarouselResponse = listOf(
                RecommendationWidget(recommendationItemList = firstProductCarouselItemList),
                RecommendationWidget(recommendationItemList = emptyList())
            )

            val secondProductCarouselItemList = listOf(
                RecommendationItem(
                    productId = 1,
                    parentID = 0,
                    shopId = 3,
                    name = "Tahu Kotak",
                    appUrl = "tokopedia://product/detail/2"
                )
            )

            val secondProductCarouselResponse = listOf(
                RecommendationWidget(recommendationItemList = secondProductCarouselItemList),
                RecommendationWidget(recommendationItemList = emptyList())
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = channelId,
                    layout = layout,
                    header = Header(
                        id = "5",
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "1",
                            name = "Sayur-sayuran",
                            param = "?pageName=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pageName=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pageName=page_name3&categoryIDs=541"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(firstProductCarouselResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            onGetRecommendation_thenReturn(secondProductCarouselResponse)

            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = selectedChipId)
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            val chipList = listOf(
                TokoNowChipUiModel(
                    id = "1",
                    text = "Sayur-sayuran",
                    param = "?pageName=page_name1",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "2",
                    text = "Buah-buahan",
                    param = "?pageName=page_name2",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "3",
                    text = "Bumbu Dapur",
                    param = "?pageName=page_name3&categoryIDs=541",
                    selected = true
                )
            )

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "3",
                    shopType = "",
                    appLink = "tokopedia://product/detail/2",
                    headerName = "Lagi Diskon",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "1",
                        name = "Tahu Kotak",
                        isVariant = false,
                        price = "",
                        orderQuantity = 0,
                        usePreDraw = true,
                        needToShowQuantityEditor = true,
                        needToChangeMaxLinesName = true
                    ),
                    parentId = "0"
                )
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

            val productCarouselChipsUiModel = HomeProductCarouselChipsUiModel(
                id = "1001",
                header = headerUiModel,
                chipList = chipList,
                carouselItemList = productList,
                state = TokoNowProductRecommendationState.LOADED
            )

            val homeLayoutItems = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                productCarouselChipsUiModel
            )

            val expectedResult = Success(
                HomeLayoutListUiModel(
                    items = homeLayoutItems,
                    state = TokoNowLayoutState.UPDATE
                )
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetCarouselChipsRecomCalled(
                pageName = "page_name3",
                categoryIDs = listOf("541")
            )

            viewModel.homeLayoutList
                .verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `given chip carousel when switchProductCarouselChipTab multiple times should call getRecommendationUseCase once`() {
        runTest {
            val channelId = "1001"
            val layout = "chip_carousel"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = channelId,
                    layout = layout,
                    header = Header(
                        id = "5",
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "1",
                            name = "Sayur-sayuran",
                            param = "?pageName=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pageName=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pageName=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = "2")
            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = "1")
            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = "3")
            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = "2")
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetCarouselChipsRecomCalled(
                times = 1,
                pageName = "page_name2"
            )
        }
    }

    @Test
    fun `given chip carousel when switchProductCarouselChipTab error should do nothing`() {
        runTest {
            val channelId = "1001"
            val layout = "chip_carousel"

            val firstProductCarouselItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    parentID = 1,
                    shopId = 5,
                    name = "Tahu Bulat",
                    appUrl = "tokopedia://product/detail/1"
                )
            )

            val firstProductCarouselResponse = listOf(
                RecommendationWidget(recommendationItemList = firstProductCarouselItemList),
                RecommendationWidget(recommendationItemList = emptyList())
            )

            val secondProductCarouselResponse = NullPointerException()

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = channelId,
                    layout = layout,
                    header = Header(
                        id = "5",
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "1",
                            name = "Sayur-sayuran",
                            param = "?pageName=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pageName=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pageName=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(firstProductCarouselResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            onGetRecommendation_thenReturn(secondProductCarouselResponse)

            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = "3")
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            val chipList = listOf(
                TokoNowChipUiModel(
                    id = "1",
                    text = "Sayur-sayuran",
                    param = "?pageName=page_name1",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "2",
                    text = "Buah-buahan",
                    param = "?pageName=page_name2",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "3",
                    text = "Bumbu Dapur",
                    param = "?pageName=page_name3",
                    selected = true
                )
            )

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    shopType = "",
                    appLink = "tokopedia://product/detail/1",
                    headerName = "Lagi Diskon",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "5",
                        name = "Tahu Bulat",
                        isVariant = true,
                        price = "",
                        orderQuantity = 0,
                        usePreDraw = true,
                        needToShowQuantityEditor = true,
                        needToChangeMaxLinesName = true
                    ),
                    parentId = "1"
                )
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

            val productCarouselChipsUiModel = HomeProductCarouselChipsUiModel(
                id = "1001",
                header = headerUiModel,
                chipList = chipList,
                carouselItemList = productList,
                state = TokoNowProductRecommendationState.LOADING
            )

            val homeLayoutItems = listOf(
                TokoNowChooseAddressWidgetUiModel(id = "0"),
                productCarouselChipsUiModel
            )

            val expectedResult = Success(
                HomeLayoutListUiModel(
                    items = homeLayoutItems,
                    state = TokoNowLayoutState.UPDATE
                )
            )

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetCarouselChipsRecomCalled(
                pageName = "page_name3"
            )

            viewModel.homeLayoutList
                .verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `given selected chip not found when switchProductCarouselChipTab should do nothing`() {
        coroutineTestRule.runTest {
            val selectedChipId = "99"

            val channelId = "1001"
            val layout = "chip_carousel"

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = channelId,
                    layout = layout,
                    header = Header(
                        id = "5",
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "1",
                            name = "Sayur-sayuran",
                            param = "?pageName=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pageName=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pageName=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = selectedChipId)
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
        }
    }

    @Test
    fun `given chip carousel when add to cart should update homeAddToCartTracker live data`() {
        runTest {
            val channelId = "1001"
            val productId = "5"
            val shopId = "5"
            val quantity = 2
            val stock = 5

            val productCarouselItemList = listOf(
                RecommendationItem(
                    productId = 1,
                    parentID = 1,
                    shopId = 2,
                    name = "Tahu Kotak",
                    appUrl = "tokopedia://product/detail/1"
                ),
                RecommendationItem(
                    productId = productId.toLong(),
                    parentID = 1,
                    shopId = shopId.toInt(),
                    name = "Tahu Bulat",
                    appUrl = "tokopedia://product/detail/2",
                    stock = stock
                )
            )

            val productCarouselResponse = listOf(
                RecommendationWidget(recommendationItemList = productCarouselItemList),
                RecommendationWidget(recommendationItemList = emptyList())
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = channelId,
                    layout = "chip_carousel",
                    header = Header(
                        id = "5",
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "1",
                            name = "Sayur-sayuran",
                            param = "?pageName=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pageName=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pageName=page_name3"
                        )
                    )
                )
            )
            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "9"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(productCarouselResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(
                productId = productId,
                quantity = quantity,
                shopId = shopId,
                stock = stock,
                isVariant = true,
                type = TokoNowLayoutType.CHIP_CAROUSEL
            )
            advanceTimeBy(ADD_TO_CART_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetCarouselChipsRecomCalled(
                pageName = "page_name1"
            )

            val chipList = listOf(
                TokoNowChipUiModel(
                    id = "1",
                    text = "Sayur-sayuran",
                    param = "?pageName=page_name1",
                    selected = true
                ),
                TokoNowChipUiModel(
                    id = "2",
                    text = "Buah-buahan",
                    param = "?pageName=page_name2",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "3",
                    text = "Bumbu Dapur",
                    param = "?pageName=page_name3",
                    selected = false
                )
            )

            val productList = listOf(
                ProductCardCompactCarouselItemUiModel(
                    shopId = "2",
                    shopType = "",
                    appLink = "tokopedia://product/detail/1",
                    headerName = "Lagi Diskon",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "1",
                        name = "Tahu Kotak",
                        isVariant = true,
                        price = "",
                        orderQuantity = 0,
                        usePreDraw = true,
                        needToShowQuantityEditor = true,
                        needToChangeMaxLinesName = true
                    ),
                    parentId = "1"
                ),
                ProductCardCompactCarouselItemUiModel(
                    shopId = "5",
                    shopType = "",
                    appLink = "tokopedia://product/detail/2",
                    headerName = "Lagi Diskon",
                    productCardModel = ProductCardCompactUiModel(
                        productId = "5",
                        name = "Tahu Bulat",
                        isVariant = true,
                        price = "",
                        orderQuantity = 2,
                        usePreDraw = true,
                        needToShowQuantityEditor = true,
                        needToChangeMaxLinesName = true,
                        availableStock = 5
                    ),
                    parentId = "1"
                )
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

            val carouselChipsUiModel = HomeProductCarouselChipsUiModel(
                id = "1001",
                header = headerUiModel,
                chipList = chipList,
                carouselItemList = productList,
                state = TokoNowProductRecommendationState.LOADED
            )

            val expectedResult = HomeAddToCartTracker(
                position = 1,
                quantity = quantity,
                cartId = "9",
                data = carouselChipsUiModel
            )

            viewModel.homeAddToCartTracker
                .verifyValueEquals(expectedResult)
        }
    }

    @Test
    fun `given product not found when add to cart should NOT update homeAddToCartTracker live data`() {
        coroutineTestRule.runTest {
            val channelId = "1001"
            val productId = "5"
            val shopId = "5"
            val quantity = 2

            val productCarouselItemList = listOf(
                RecommendationItem(
                    productId = 1,
                    parentID = 1,
                    shopId = 2,
                    name = "Tahu Kotak",
                    appUrl = "tokopedia://product/detail/1"
                ),
                RecommendationItem(
                    productId = productId.toLong(),
                    parentID = 1,
                    shopId = shopId.toInt(),
                    name = "Tahu Bulat",
                    appUrl = "tokopedia://product/detail/2"
                )
            )

            val productCarouselResponse = listOf(
                RecommendationWidget(recommendationItemList = productCarouselItemList),
                RecommendationWidget(recommendationItemList = emptyList())
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = channelId,
                    layout = "chip_carousel",
                    header = Header(
                        id = "5",
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = "1",
                            name = "Sayur-sayuran",
                            param = "?pageName=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pageName=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pageName=page_name3"
                        )
                    )
                )
            )
            val addToCartResponse = AddToCartDataModel(data = DataModel(cartId = "9"))

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(productCarouselResponse)
            onAddToCart_thenReturn(addToCartResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
            viewModel.onCartQuantityChanged(
                productId = "99",
                quantity = quantity,
                shopId = shopId,
                stock = 0,
                isVariant = false,
                type = TokoNowLayoutType.CHIP_CAROUSEL
            )
            advanceTimeBy(ADD_TO_CART_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()

            viewModel.homeAddToCartTracker
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `given chipId same with current selected chipId when switchProductCarouselChipTab should NOT call get getRecommendationUseCase`() {
        coroutineTestRule.runTest {
            val chipId = "1"
            val currentSelectedChipId = "1"

            val channelId = "1001"
            val layout = "chip_carousel"

            val productCarouselItemList = listOf(
                RecommendationItem(
                    productId = 5,
                    parentID = 1,
                    shopId = 5,
                    name = "Tahu Bulat",
                    appUrl = "tokopedia://product/detail/1"
                )
            )

            val productCarouselResponse = listOf(
                RecommendationWidget(recommendationItemList = productCarouselItemList),
                RecommendationWidget(recommendationItemList = emptyList())
            )

            val homeLayoutResponse = listOf(
                HomeLayoutResponse(
                    id = channelId,
                    layout = layout,
                    header = Header(
                        id = "5",
                        name = "Lagi Diskon",
                        serverTimeUnix = 0
                    ),
                    grids = listOf(
                        Grid(
                            id = currentSelectedChipId,
                            name = "Sayur-sayuran",
                            param = "?pageName=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pageName=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pageName=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(productCarouselResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = chipId)
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetCarouselChipsRecomNotCalled(
                pageName = "page_name1"
            )
        }
    }

    @Test
    fun `given chip carousel not found when switchProductCarouselChipTab should NOT call get getRecommendationUseCase`() {
        coroutineTestRule.runTest {
            val chipId = "1"
            val channelId = "1001"
            val homeLayoutResponse = emptyList<HomeLayoutResponse>()

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)

            viewModel.getHomeLayout(
                localCacheModel = LocalCacheModel(),
                removeAbleWidgets = listOf(),
                enableNewRepurchase = true
            )
            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = chipId)
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
            verifyGetCarouselChipsRecomNotCalled()
        }
    }
}

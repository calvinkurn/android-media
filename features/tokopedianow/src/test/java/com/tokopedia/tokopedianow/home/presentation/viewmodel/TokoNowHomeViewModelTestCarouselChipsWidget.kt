package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTestCarouselChipsWidget : TokoNowHomeViewModelTestFixture() {

    companion object {
        private const val SWITCH_PRODUCT_CAROUSEL_TAB_DELAY = 500L
    }

    @Test
    fun `given chip carousel layout response when getLayoutComponentData should map carousel chips widget to homeLayoutList`() {
        coroutineTestRule.runBlockingTest {
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
                            param = "?pagename=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pagename=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pagename=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(productCarouselResponse)

            viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            val chipList = listOf(
                TokoNowChipUiModel(
                    id = "1",
                    text = "Sayur-sayuran",
                    param = "page_name1",
                    selected = true
                ),
                TokoNowChipUiModel(
                    id = "2",
                    text = "Buah-buahan",
                    param = "page_name2",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "3",
                    text = "Bumbu Dapur",
                    param = "page_name3",
                    selected = false
                )
            )

            val productList = listOf(
                TokoNowProductCardCarouselItemUiModel(
                    shopId = "5",
                    shopType = "",
                    appLink = "tokopedia://product/detail/1",
                    headerName = "Lagi Diskon",
                    productCardModel = TokoNowProductCardViewUiModel(
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
        coroutineTestRule.runBlockingTest {
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
                            param = "?pagename=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pagename=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pagename=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(firstProductCarouselResponse)

            viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            onGetRecommendation_thenReturn(secondProductCarouselResponse)

            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = selectedChipId)
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            val chipList = listOf(
                TokoNowChipUiModel(
                    id = "1",
                    text = "Sayur-sayuran",
                    param = "page_name1",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "2",
                    text = "Buah-buahan",
                    param = "page_name2",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "3",
                    text = "Bumbu Dapur",
                    param = "page_name3",
                    selected = true
                )
            )

            val productList = listOf(
                TokoNowProductCardCarouselItemUiModel(
                    shopId = "3",
                    shopType = "",
                    appLink = "tokopedia://product/detail/2",
                    headerName = "Lagi Diskon",
                    productCardModel = TokoNowProductCardViewUiModel(
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
                pageName = "page_name3"
            )

            viewModel.homeLayoutList
                .verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `given chip carousel when switchProductCarouselChipTab multiple times should call getRecommendationUseCase once`() {
        coroutineTestRule.runBlockingTest {
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
                            param = "?pagename=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pagename=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pagename=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)

            viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
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
        coroutineTestRule.runBlockingTest {
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
                            param = "?pagename=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pagename=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pagename=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)
            onGetRecommendation_thenReturn(firstProductCarouselResponse)

            viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            onGetRecommendation_thenReturn(secondProductCarouselResponse)

            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = "3")
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            val chipList = listOf(
                TokoNowChipUiModel(
                    id = "1",
                    text = "Sayur-sayuran",
                    param = "page_name1",
                    selected = true
                ),
                TokoNowChipUiModel(
                    id = "2",
                    text = "Buah-buahan",
                    param = "page_name2",
                    selected = false
                ),
                TokoNowChipUiModel(
                    id = "3",
                    text = "Bumbu Dapur",
                    param = "page_name3",
                    selected = false
                )
            )

            val productList = listOf(
                TokoNowProductCardCarouselItemUiModel(
                    shopId = "5",
                    shopType = "",
                    appLink = "tokopedia://product/detail/1",
                    headerName = "Lagi Diskon",
                    productCardModel = TokoNowProductCardViewUiModel(
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
        coroutineTestRule.runBlockingTest {
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
                            param = "?pagename=page_name1"
                        ),
                        Grid(
                            id = "2",
                            name = "Buah-buahan",
                            param = "?pagename=page_name2"
                        ),
                        Grid(
                            id = "3",
                            name = "Bumbu Dapur",
                            param = "?pagename=page_name3"
                        )
                    )
                )
            )

            onGetHomeLayoutData_thenReturn(homeLayoutResponse)

            viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
            viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())

            viewModel.switchProductCarouselChipTab(channelId = channelId, chipId = selectedChipId)
            advanceTimeBy(SWITCH_PRODUCT_CAROUSEL_TAB_DELAY)

            verifyGetHomeLayoutDataUseCaseCalled()
        }
    }
}

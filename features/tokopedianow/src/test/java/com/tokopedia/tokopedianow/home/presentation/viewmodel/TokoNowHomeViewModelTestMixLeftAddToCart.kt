package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.data.createLeftCarouselAtcDataModel
import com.tokopedia.tokopedianow.home.domain.model.Grid
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.domain.model.Shop
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSpaceUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class TokoNowHomeViewModelTestMixLeftAddToCart : TokoNowHomeViewModelTestFixture() {

    @Test
    fun `given mix left carousel atc when addProductToCart should update all product quantity`() {
        val quantity = 5
        val type = TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC

        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = "1002",
                layout = "left_carousel_atc",
                header = Header(
                    name = "Mix Left Carousel",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(id = "3", name = "Product 1", shop = Shop(shopId = "1"))
                )
            ),
            HomeLayoutResponse(
                id = "1003",
                layout = "left_carousel_atc",
                header = Header(
                    name = "Mix Left Carousel 2",
                    serverTimeUnix = 0
                ),
                grids = listOf(
                    Grid(id = "5", name = "Product 2", shop = Shop(shopId = "2"))
                )
            )
        )

        val addToCartResponse = AddToCartDataModel()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onAddToCart_thenReturn(addToCartResponse)

        viewModel.getHomeLayout(localCacheModel = LocalCacheModel(), removeAbleWidgets = listOf())
        viewModel.getLayoutComponentData(localCacheModel = LocalCacheModel())
        viewModel.addProductToCart("", "3", quantity, "1", type)
        viewModel.addProductToCart("", "5", quantity, "2", type)

        val chooseAddressWidget = TokoNowChooseAddressWidgetUiModel(id = "0")

        val firstMixLeftCarousel = createLeftCarouselAtcDataModel(
            id = "1002",
            headerName = "Mix Left Carousel",
            productList = listOf(
                HomeLeftCarouselAtcProductCardSpaceUiModel(
                    channelId = "1002",
                    channelHeaderName = "Mix Left Carousel",
                ),
                HomeLeftCarouselAtcProductCardUiModel(
                    id = "3",
                    shopId = "1",
                    channelId = "1002",
                    channelHeaderName = "Mix Left Carousel",
                    productCardModel = ProductCardModel(
                        productName = "Product 1",
                        nonVariant = ProductCardModel.NonVariant(quantity = quantity),
                        formattedPrice = "0"
                    ),
                    position = 1
                )
            )
        )

        val secondMixLeftCarousel = createLeftCarouselAtcDataModel(
            id = "1003",
            headerName = "Mix Left Carousel 2",
            productList = listOf(
                HomeLeftCarouselAtcProductCardSpaceUiModel(
                    channelId = "1003",
                    channelHeaderName = "Mix Left Carousel 2",
                ),
                HomeLeftCarouselAtcProductCardUiModel(
                    id = "5",
                    shopId = "2",
                    channelId = "1003",
                    channelHeaderName = "Mix Left Carousel 2",
                    productCardModel = ProductCardModel(
                        productName = "Product 2",
                        nonVariant = ProductCardModel.NonVariant(quantity = quantity),
                        formattedPrice = "0"
                    ),
                    position = 1
                )
            )
        )

        val homeLayoutItems = listOf(
            chooseAddressWidget,
            firstMixLeftCarousel,
            secondMixLeftCarousel
        )

        val expectedResult = Success(
            HomeLayoutListUiModel(
                items = homeLayoutItems,
                state = TokoNowLayoutState.UPDATE
            )
        )

        verifyGetHomeLayoutDataUseCaseCalled()
        verifyAddToCartUseCaseCalled()

        viewModel.atcQuantity
            .verifySuccessEquals(expectedResult)
    }
}

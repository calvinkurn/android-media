package com.tokopedia.tokopedianow.home.presentation.viewmodel

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.domain.model.RepurchaseProduct
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.data.*
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.DEFAULT_QUANTITY
import com.tokopedia.tokopedianow.home.domain.model.GetRepurchaseResponse
import com.tokopedia.tokopedianow.home.domain.model.Header
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutListUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowHomeViewModelTestTicker : TokoNowHomeViewModelTestFixture() {
    companion object {
        private const val CHANGE_QUANTITY_DELAY = 500L
    }

    @Test
    fun `given error exception when calling getTicker after getting getLayoutData then it should deny to add ticker widget on home layout list`() {
        val dynamicLegoBannerId = "34923"
        val dynamicLegoBannerTitle = "Lego Banner"
        val categoryGridId = "11111"
        val categoryGridTitle = "Category Tokonow"
        val categorySliderBannerId = "2222"
        val categorySliderBannerTitle = "Banner Tokonow"

        onGetHomeLayoutData_thenReturn(
            layoutResponse = createHomeLayoutList()
        )
        onGetTicker_thenReturn(
            errorThrowable = NullPointerException()
        )

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf(),
            enableNewRepurchase = true
        )
        viewModel.getLayoutComponentData(
            localCacheModel = LocalCacheModel()
        )

        val expectedResult = HomeLayoutListUiModel(
            items = listOf(
                TokoNowChooseAddressWidgetUiModel(
                    id = CHOOSE_ADDRESS_WIDGET_ID
                ),
                createDynamicLegoBannerDataModel(
                    id = dynamicLegoBannerId,
                    groupId = String.EMPTY,
                    headerName = dynamicLegoBannerTitle
                ),
                createCategoryGridDataModel(
                    id = categoryGridId,
                    title = categoryGridTitle,
                    categoryList = null,
                    state = TokoNowLayoutState.HIDE
                ),
                createSliderBannerDataModel(
                    id = categorySliderBannerId,
                    groupId = String.EMPTY,
                    headerName = categorySliderBannerTitle
                )
            ),
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)
        verifyBlockAddToCartNull()
    }

    @Test
    fun `given ticker response when calling getTicker after getting getLayoutData then it should add ticker widget on home layout list`() = runTest {
        val repurchaseChannelId = "1001"
        val repurchaseProductId = "1"
        val repurchaseProductPosition = 1
        val repurchaseProductTitle = "Kamu pernah beli"
        val repurchaseProductMaxOrder = 4
        val repurchaseProductMinOrder = 3
        val repurchaseProductStock = 4
        val repurchaseProductIsVariant = false
        val repurchaseProductShopId = "5"
        val repurchaseProductType = TokoNowLayoutType.REPURCHASE_PRODUCT
        val repurchaseProductQuantityChanged = 10
        val repurchaseLayout = "recent_purchase_tokonow"

        val tickerResponse = createTicker()
        val tickerList = TickerMapper.mapTickerData(tickerResponse).second
        val homeLayoutResponse = listOf(
            HomeLayoutResponse(
                id = repurchaseChannelId,
                layout = repurchaseLayout,
                header = Header(
                    name = repurchaseProductTitle,
                    serverTimeUnix = 0
                )
            )
        )
        val repurchaseResponse = GetRepurchaseResponse.RepurchaseData(
            title = repurchaseProductTitle,
            products = listOf(
                RepurchaseProduct(
                    id = repurchaseProductId,
                    stock = repurchaseProductMaxOrder,
                    maxOrder = repurchaseProductMaxOrder,
                    minOrder = repurchaseProductMinOrder
                )
            )
        )
        val addToCartResponse = AddToCartDataModel()

        onGetHomeLayoutData_thenReturn(homeLayoutResponse)
        onGetRepurchaseWidget_thenReturn(repurchaseResponse)
        onAddToCart_thenReturn(addToCartResponse)
        onGetTicker_thenReturn(tickerResponse)

        viewModel.getHomeLayout(
            localCacheModel = LocalCacheModel(),
            removeAbleWidgets = listOf(),
            enableNewRepurchase = true
        )
        viewModel.getLayoutComponentData(
            localCacheModel = LocalCacheModel()
        )
        viewModel.onCartQuantityChanged(
            channelId = repurchaseChannelId,
            productId = repurchaseProductId,
            quantity = repurchaseProductQuantityChanged,
            shopId = repurchaseProductShopId,
            type = repurchaseProductType,
            stock = repurchaseProductStock,
            isVariant = repurchaseProductIsVariant
        )
        advanceTimeBy(CHANGE_QUANTITY_DELAY)

        val homeLayoutItems = listOf(
            TokoNowChooseAddressWidgetUiModel(
                id = CHOOSE_ADDRESS_WIDGET_ID
            ),
            TokoNowTickerUiModel(
                id = HomeStaticLayoutId.TICKER_WIDGET_ID,
                tickers = tickerList
            ),
            TokoNowRepurchaseUiModel(
                id = repurchaseChannelId,
                title = repurchaseProductTitle,
                productList = listOf(
                    createHomeProductCardUiModel(
                        channelId = repurchaseChannelId,
                        productId = repurchaseProductId,
                        quantity = DEFAULT_QUANTITY,
                        stock = repurchaseProductStock,
                        minOrder = repurchaseProductMinOrder,
                        maxOrder = repurchaseProductMaxOrder,
                        position = repurchaseProductPosition,
                        originalPosition = repurchaseProductPosition,
                        headerName = repurchaseProductTitle
                    )
                ),
                state = TokoNowLayoutState.SHOW
            )
        )

        val expectedResult = HomeLayoutListUiModel(
            items = homeLayoutItems,
            state = TokoNowLayoutState.UPDATE
        )

        verifyGetTickerUseCaseCalled()
        verifyGetHomeLayoutDataUseCaseCalled()
        verifyGetRepurchaseWidgetUseCaseCalled()
        verifyGetHomeLayoutResponseSuccess(expectedResult)
        verifyBlockAddToCartNotNull()
    }

}

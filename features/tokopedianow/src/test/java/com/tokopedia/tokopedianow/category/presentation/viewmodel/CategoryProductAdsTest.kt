package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToTicker
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.util.AddToCartMapper
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.util.ProductAdsMapper
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryProductAdsTest : TokoNowCategoryMainViewModelTestFixture() {

    @Test
    fun `given get product ads success when getFirstPage should add product ads carousel to categoryPage`() {
        val isLoggedIn = true
        val userId = "12223"
        val deviceId = "11111"
        val shopId = "1502"
        val getProductAdsResponse = getProductAdsResponse.productAds

        val warehouses = listOf(
            LocalWarehouseModel(
                warehouse_id = 15125512,
                service_type = "fc"
            ),
            LocalWarehouseModel(
                warehouse_id = 14231455,
                service_type = "hub"
            )
        )

        setAddressData(
            warehouseId = warehouseId,
            shopId = shopId,
            warehouses = warehouses
        )

        onUserSession_thenReturns(
            isLoggedIn = isLoggedIn,
            userId = userId,
            deviceId = deviceId
        )
        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onGetProductAds_thenReturn(getProductAdsResponse)

        viewModel.onViewCreated(
            navToolbarHeight = navToolbarHeight
        )
        viewModel.getFirstPage()

        // map header space
        val headerSpaceUiModel = categoryDetailResponse
            .mapToHeaderSpace(
                space = navToolbarHeight
            )

        // map choose address
        val chooseAddressUiModel = categoryDetailResponse
            .mapToChooseAddress()

        // map ticker
        val tickerDataList = TickerMapper.mapTickerData(
            tickerList = targetedTickerResponse
        )
        val tickerUiModel = categoryDetailResponse
            .mapToTicker(
                tickerData = tickerDataList
            )
        val hasBlockedAddToCart = tickerDataList.first

        // map title
        val titleUiModel = categoryDetailResponse
            .mapToCategoryTitle()

        // map category navigation
        val categoryNavigationUiModel = categoryDetailResponse
            .mapToCategoryNavigation()

        // map product recommendation
        val productRecommendationUiModel = ProductRecommendationMapper.createProductRecommendation(
            categoryIds = listOf(categoryIdL1)
        )

        val productAdsUiModel = ProductAdsMapper.mapProductAdsCarousel(
            response = getProductAdsResponse,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        val categoryPage = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            tickerUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel,
            productAdsUiModel
        )

        val expectedGetProductAdsParam = GetProductAdsParam(
            categoryId = categoryIdL1,
            src = "directory_tokonow",
            page = 1,
            userId = userId,
            addressData = addressData
        ).generateQueryParams()

        verifyGetProductAdsParam(expectedGetProductAdsParam)

        viewModel.categoryPage
            .verifyValueEquals(categoryPage)
    }

    @Test
    fun `given get product ads return empty product when getFirstPage should remove product ads carousel from categoryPage`() {
        val isLoggedIn = true
        val userId = "12223"
        val deviceId = "11111"
        val shopId = "1502"
        val getProductAdsResponse = getProductAdsResponse
            .productAds.copy(productList = emptyList())

        val warehouses = listOf(
            LocalWarehouseModel(
                warehouse_id = 15125512,
                service_type = "fc"
            ),
            LocalWarehouseModel(
                warehouse_id = 14231455,
                service_type = "hub"
            )
        )

        setAddressData(
            warehouseId = warehouseId,
            shopId = shopId,
            warehouses = warehouses
        )

        onUserSession_thenReturns(
            isLoggedIn = isLoggedIn,
            userId = userId,
            deviceId = deviceId
        )
        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onGetProductAds_thenReturn(getProductAdsResponse)

        viewModel.onViewCreated(
            navToolbarHeight = navToolbarHeight
        )
        viewModel.getFirstPage()

        // map header space
        val headerSpaceUiModel = categoryDetailResponse
            .mapToHeaderSpace(
                space = navToolbarHeight
            )

        // map choose address
        val chooseAddressUiModel = categoryDetailResponse
            .mapToChooseAddress()

        // map ticker
        val tickerDataList = TickerMapper.mapTickerData(
            tickerList = targetedTickerResponse
        )
        val tickerUiModel = categoryDetailResponse
            .mapToTicker(
                tickerData = tickerDataList
            )

        // map title
        val titleUiModel = categoryDetailResponse
            .mapToCategoryTitle()

        // map category navigation
        val categoryNavigationUiModel = categoryDetailResponse
            .mapToCategoryNavigation()

        // map product recommendation
        val productRecommendationUiModel = ProductRecommendationMapper.createProductRecommendation(
            categoryIds = listOf(categoryIdL1)
        )

        val categoryPage = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            tickerUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel
        )

        val expectedGetProductAdsParam = GetProductAdsParam(
            categoryId = categoryIdL1,
            src = "directory_tokonow",
            page = 1,
            userId = userId,
            addressData = addressData
        ).generateQueryParams()

        verifyGetProductAdsParam(expectedGetProductAdsParam)

        viewModel.categoryPage
            .verifyValueEquals(categoryPage)
    }

    @Test
    fun `given product ads when add product to cart should update orderQuantity and atc tracker data`() {
        runTest {
            val productQuantity = 1

            val getProductAdsResponse = getProductAdsResponse.productAds
            val addToCartDataModel = AddToCartMapper.mapAddToCartResponse(addToCartGqlResponse)
            onAddToCart_thenReturns(addToCartDataModel)

            val productAdsCarousel = ProductAdsMapper.mapProductAdsCarousel(getProductAdsResponse)
            val adsProductItem = productAdsCarousel.items.first()

            viewModel.mockPrivateField(
                name = "layout",
                value = mutableListOf(productAdsCarousel)
            )

            viewModel.onCartQuantityChanged(
                product = adsProductItem.productCardModel,
                shopId = adsProductItem.shopId,
                quantity = productQuantity,
                layoutType = PRODUCT_ADS_CAROUSEL
            )

            val expectedProductCardModel = adsProductItem.productCardModel.copy(orderQuantity = 1)
            val expectedAdsProduct = adsProductItem.copy(productCardModel = expectedProductCardModel)
            val expectedProductAdsCarousel = productAdsCarousel.copy(items = listOf(expectedAdsProduct))
            val expectedCategoryPage = listOf(expectedProductAdsCarousel)

            val expectedAtcTrackerData = CategoryAtcTrackerModel(
                index = adsProductItem.position,
                quantity = productQuantity,
                shopId = adsProductItem.shopId,
                shopName = adsProductItem.shopName,
                shopType = adsProductItem.shopType,
                categoryBreadcrumbs = adsProductItem.categoryBreadcrumbs,
                product = expectedProductCardModel,
                layoutType = PRODUCT_ADS_CAROUSEL
            )

            viewModel.addItemToCart.getOrAwaitValue()
            viewModel.updateToolbarNotification.getOrAwaitValue()
            viewModel.atcDataTracker.getOrAwaitValue()
            viewModel.categoryPage.getOrAwaitValue()

            viewModel.addItemToCart
                .verifyValueEquals(Success(addToCartDataModel))

            viewModel.updateToolbarNotification
                .verifyValueEquals(true)

            viewModel.atcDataTracker
                .verifyValueEquals(expectedAtcTrackerData)

            viewModel.categoryPage
                .verifyValueEquals(expectedCategoryPage)
        }
    }

    @Test
    fun `given product ads not found when add product to cart should not update atc tracker data`() {
        runTest {
            val productQuantity = 1

            val getProductAdsResponse = getProductAdsResponse.productAds
            val addToCartDataModel = AddToCartMapper.mapAddToCartResponse(addToCartGqlResponse)
            onAddToCart_thenReturns(addToCartDataModel)

            val productAdsCarousel = ProductAdsMapper.mapProductAdsCarousel(getProductAdsResponse)
            val adsProductItem = productAdsCarousel.items.first()

            viewModel.mockPrivateField(
                name = "layout",
                value = mutableListOf(productAdsCarousel)
            )

            viewModel.onCartQuantityChanged(
                product = adsProductItem.productCardModel,
                shopId = adsProductItem.shopId,
                quantity = productQuantity,
                layoutType = PRODUCT_ADS_CAROUSEL
            )

            viewModel.atcDataTracker
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `given unknown layout type when add product to cart should not update atc tracker data`() {
        runTest {
            val layoutType = "unknown"
            val productQuantity = 1

            val getProductAdsResponse = getProductAdsResponse.productAds
            val addToCartDataModel = AddToCartMapper.mapAddToCartResponse(addToCartGqlResponse)
            onAddToCart_thenReturns(addToCartDataModel)

            val productAdsCarousel = ProductAdsMapper.mapProductAdsCarousel(getProductAdsResponse)
            val adsProductItem = productAdsCarousel.items.first()

            viewModel.mockPrivateField(
                name = "layout",
                value = mutableListOf(productAdsCarousel)
            )

            viewModel.onCartQuantityChanged(
                product = adsProductItem.productCardModel,
                shopId = adsProductItem.shopId,
                quantity = productQuantity,
                layoutType = layoutType
            )

            viewModel.atcDataTracker
                .verifyValueEquals(null)
        }
    }
}

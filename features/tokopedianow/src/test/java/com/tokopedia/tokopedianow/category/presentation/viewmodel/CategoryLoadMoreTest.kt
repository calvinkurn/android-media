package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToTicker
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryRecommendationMapper.mapToCategoryRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class CategoryLoadMoreTest: TokoNowCategoryMainViewModelTestFixture() {

    @Test
    fun `loadMore should load more showcases and not prevent user to scroll more`() {
        val isLoggedIn = true
        val userId = "12223"
        val deviceId = "11111"

        setAddressData(
            warehouseId = warehouseId,
            warehouses = getLocalWarehouseModelList(),
            shopId = shopId
        )
        onUserSession_thenReturns(
            isLoggedIn = isLoggedIn,
            userId = userId,
            deviceId = deviceId
        )
        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onCategoryProduct_thenReturns(
            uniqueId = getUniqueId(
                isLoggedIn = isLoggedIn,
                userId = userId,
                deviceId = deviceId
            )
        )

        viewModel.onViewCreated(
            navToolbarHeight = navToolbarHeight
        )
        viewModel.getFirstPage()
        viewModel.loadMore(true)

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

        val resultList = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            tickerUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel
        )

        val categoryNavigationList = categoryNavigationUiModel.categoryListUiModel.toMutableList()

        // first page
        mapShowcaseProduct(
            hasAdded = true,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        // load more
        mapShowcaseProduct(
            hasAdded = true,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.scrollNotNeeded
            .verifyValueEquals(null)
        viewModel.categoryPage
            .verifyValueEquals(resultList)
    }

    @Test
    fun `loadMore should load all showcases plus category recommendation and prevent user to scroll more`() {
        val isLoggedIn = true
        val userId = "12223"
        val deviceId = "11111"

        setAddressData(
            warehouseId = warehouseId,
            warehouses = getLocalWarehouseModelList(),
            shopId = shopId
        )
        onUserSession_thenReturns(
            isLoggedIn = isLoggedIn,
            userId = userId,
            deviceId = deviceId
        )
        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()
        onCategoryProduct_thenReturns(
            uniqueId = getUniqueId(
                isLoggedIn = isLoggedIn,
                userId = userId,
                deviceId = deviceId
            )
        )

        viewModel.onViewCreated(
            navToolbarHeight = navToolbarHeight
        )
        viewModel.getFirstPage()
        viewModel.loadMore(true)
        viewModel.loadMore(true)

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

        // map category recommendation
        val categoryRecommendationUiModel = categoryDetailResponse
            .mapToCategoryRecommendation()

        val resultList = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            tickerUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel,
        )

        val categoryNavigationList = categoryNavigationUiModel.categoryListUiModel.toMutableList()

        // first page
        mapShowcaseProduct(
            hasAdded = true,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        // load more
        mapShowcaseProduct(
            hasAdded = true,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        // load more
        mapShowcaseProduct(
            hasAdded = true,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        // add the latest widget (category recommendation)
        resultList.add(categoryRecommendationUiModel)

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.scrollNotNeeded
            .verifyValueEquals(Unit)
        viewModel.categoryPage
            .verifyValueEquals(resultList)
    }


    @Test
    fun `loadMore with not being at the bottom of the page should do nothing`() {
        viewModel.loadMore(false)

        viewModel.scrollNotNeeded
            .verifyValueEquals(null)
    }

    @Test
    fun `loadMore with being at the bottom of the page and categoryL2 model is empty should prevent user to scroll more`() {
        viewModel.loadMore(true)

        viewModel.scrollNotNeeded
            .verifyValueEquals(Unit)
    }

    @Test
    fun `modify layout while its value is null should make loadMore error and do nothing`() {
        val privateFieldNameLayout = "layout"

        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = null
        )

        viewModel.loadMore(true)

        viewModel.categoryPage
            .verifyValueEquals(null)
    }
}

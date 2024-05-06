package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryHeader
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryRecommendationMapper.mapToCategoryRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CategoryLoadMoreTest : TokoNowCategoryViewModelTestFixture() {

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
        onCategoryProduct_thenReturns()

        viewModel.onViewCreated()
        viewModel.onScroll(true)

        // map ticker
        val tickerDataList = TickerMapper.mapTickerData(
            targetedTickerResponse
        )

        // map header
        val header = categoryDetailResponse.mapToCategoryHeader(
            ctaText = resourceProvider.getString(R.string.tokopedianow_category_title_another_category),
            ctaTextColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_GN500),
            tickerList = tickerDataList.tickerList
        )

        val hasBlockedAddToCart = tickerDataList.blockAddToCart

        // map category navigation
        val categoryNavigationUiModel = categoryDetailResponse
            .mapToCategoryNavigation()

        // map product recommendation
        val productRecommendationUiModel = ProductRecommendationMapper.createProductRecommendation(
            categoryIds = listOf(categoryIdL1)
        )

        val resultList = mutableListOf(
            header,
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
        viewModel.visitableListLiveData
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
        onCategoryProduct_thenReturns()

        viewModel.onViewCreated()
        viewModel.onScroll(true)
        viewModel.onScroll(true)

        // map ticker
        val tickerDataList = TickerMapper.mapTickerData(
            targetedTickerResponse
        )

        val hasBlockedAddToCart = tickerDataList.blockAddToCart

        // map header
        val header = categoryDetailResponse.mapToCategoryHeader(
            ctaText = resourceProvider.getString(R.string.tokopedianow_category_title_another_category),
            ctaTextColor = resourceProvider.getColor(unifyprinciplesR.color.Unify_GN500),
            tickerList = tickerDataList.tickerList
        )

        // map category navigation
        val categoryNavigationUiModel = categoryDetailResponse
            .mapToCategoryNavigation()

        // map product recommendation
        val productRecommendationUiModel = ProductRecommendationMapper.createProductRecommendation(
            categoryIds = listOf(categoryIdL1)
        )

        // map category recommendation
        val categoryRecommendationUiModel = categoryDetailResponse
            .mapToCategoryRecommendation(source = "tokonow_category_l1")

        val resultList = mutableListOf(
            header,
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
        viewModel.visitableListLiveData
            .verifyValueEquals(resultList)
    }

    @Test
    fun `loadMore with not being at the bottom of the page should do nothing`() {
        viewModel.onScroll(false)

        viewModel.scrollNotNeeded
            .verifyValueEquals(null)
    }

    @Test
    fun `loadMore with being at the bottom of the page and categoryL2 model is empty should prevent user to scroll more`() {
        viewModel.onScroll(true)

        viewModel.scrollNotNeeded
            .verifyValueEquals(Unit)
    }

    @Test
    fun `modify layout while its value is null should make loadMore error and do nothing`() {
        val privateFieldNameLayout = "visitableList"

        viewModel.mockSuperClassField(
            name = privateFieldNameLayout,
            value = null
        )

        viewModel.onScroll(true)

        viewModel.visitableListLiveData
            .verifyValueEquals(null)
    }
}

package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToTicker
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper.createProductRecommendation
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel.Companion.NO_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.common.util.ProductAdsMapper.createProductAdsCarousel
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert
import org.junit.Test

class CategoryViewCreatedTest : TokoNowCategoryViewModelTestFixture() {

    @Test
    fun `onViewCreated should return success result with loading showcase`() {
        setAddressData(
            warehouseId = warehouseId,
            warehouses = getLocalWarehouseModelList(),
            shopId = shopId
        )
        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()

        viewModel.onViewCreated()

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
                tickerList = tickerDataList.second
            )
        val hasBlockedAddToCart = tickerDataList.first

        // map title
        val titleUiModel = categoryDetailResponse
            .mapToCategoryTitle()

        // map category navigation
        val categoryNavigationUiModel = categoryDetailResponse
            .mapToCategoryNavigation()

        // map product recommendation
        val productRecommendationUiModel = createProductRecommendation(
            categoryIds = listOf(categoryIdL1)
        )

        val productAdsCarouselUiModel = createProductAdsCarousel()

        val resultList = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            tickerUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel,
            productAdsCarouselUiModel
        )

        val categoryNavigationList = categoryNavigationUiModel.categoryListUiModel.toMutableList()
        mapShowcaseProduct(
            hasAdded = false,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.categoryFirstPage
            .verifyValueEquals(Success(resultList))
    }

    @Test
    fun `onViewCreated should return success result with loading showcase even though targeted ticker throws an error exception`() {
        setAddressData(
            warehouseId = warehouseId,
            warehouses = getLocalWarehouseModelList(),
            shopId = shopId
        )
        onCategoryDetail_thenReturns()
        onTargetedTicker_thenThrows()

        viewModel.onViewCreated()

        // Add header space
        val headerSpaceUiModel = categoryDetailResponse
            .mapToHeaderSpace(
                space = navToolbarHeight
            )

        // Add choose address
        val chooseAddressUiModel = categoryDetailResponse
            .mapToChooseAddress()

        // Add title
        val titleUiModel = categoryDetailResponse
            .mapToCategoryTitle()

        // Add category navigation
        val categoryNavigationUiModel = categoryDetailResponse
            .mapToCategoryNavigation()

        // Add product recommendation
        val productRecommendationUiModel = createProductRecommendation(
            categoryIds = listOf(categoryIdL1)
        )

        val productAdsCarouselUiModel = createProductAdsCarousel()

        val resultList = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel,
            productAdsCarouselUiModel
        )

        val categoryNavigationList = categoryNavigationUiModel.categoryListUiModel.toMutableList()
        mapShowcaseProduct(
            hasAdded = false,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = false
        )

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.categoryFirstPage
            .verifyValueEquals(Success(resultList))
    }

    @Test
    fun `onViewCreated should return failed because category detail throws an exception`() {
        onCategoryDetail_thenThrows()

        viewModel.onViewCreated()

        Assert.assertTrue(viewModel.categoryFirstPage.value is Fail)
    }

    @Test
    fun `the execution of onViewCreated should not be continued because warehouse id is zero`() {
        setAddressData(
            warehouseId = NO_WAREHOUSE_ID,
            shopId = shopId
        )

        viewModel.onViewCreated()

        viewModel.categoryFirstPage
            .verifyValueEquals(null)
        viewModel.outOfCoverageState
            .verifyValueEquals(Unit)
    }
}

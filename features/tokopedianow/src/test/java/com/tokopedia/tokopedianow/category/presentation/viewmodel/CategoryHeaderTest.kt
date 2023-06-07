package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToTicker
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper.createProductRecommendation
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryMainViewModel.Companion.NO_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import org.junit.Assert
import org.junit.Test

class CategoryHeaderTest: TokoNowCategoryMainViewModelTestFixture() {

    @Test
    fun `getCategoryHeader should return success result with loading showcase`() {
        onCategoryDetail_thenReturns()
        onTargetedTicker_thenReturns()

        viewModel.getCategoryHeader(
            navToolbarHeight = navToolbarHeight
        )

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
        val productRecommendationUiModel = createProductRecommendation(
            categoryIds = listOf(categoryIdL1)
        )

        val resultList = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            tickerUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel,
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
        viewModel.categoryHeader
            .verifyValueEquals(Success(resultList))
    }

    @Test
    fun `getCategoryHeader should return success result with loading showcase even though targeted ticker throws an error exception`() {
        onCategoryDetail_thenReturns()
        onTargetedTicker_thenThrows()

        viewModel.getCategoryHeader(
            navToolbarHeight = navToolbarHeight
        )

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

        val resultList = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel,
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
        viewModel.categoryHeader
            .verifyValueEquals(Success(resultList))
    }

    @Test
    fun `getCategoryHeader should return failed because category detail throws an exception`() {
        onCategoryDetail_thenThrows()

        viewModel.getCategoryHeader(
            navToolbarHeight = navToolbarHeight
        )

        Assert.assertTrue(viewModel.categoryHeader.value is Fail)
    }

    @Test
    fun `the execution of getCategoryHeader should not be continued because warehouse id is zero`() {
        setWarehouseId(
            warehouseId = NO_WAREHOUSE_ID
        )

        viewModel.getCategoryHeader(
            navToolbarHeight = navToolbarHeight
        )

        viewModel.categoryHeader
            .verifyValueEquals(null)
        viewModel.oosState
            .verifyValueEquals(Unit)
    }
}

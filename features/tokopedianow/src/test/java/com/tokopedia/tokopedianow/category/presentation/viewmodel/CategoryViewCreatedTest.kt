package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToTicker
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper.createProductRecommendation
import com.tokopedia.tokopedianow.category.presentation.viewmodel.TokoNowCategoryViewModel.Companion.NO_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.unit.test.ext.verifyValueEquals
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
            targetedTickerResponse
        )
        val tickerUiModel = categoryDetailResponse
            .mapToTicker(tickerDataList.tickerList)

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
            productRecommendationUiModel
        )

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.visitableListLiveData
            .verifyValueEquals(resultList)
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

        val resultList = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            titleUiModel,
            categoryNavigationUiModel,
            productRecommendationUiModel
        )

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.visitableListLiveData
            .verifyValueEquals(resultList)
    }

    @Test
    fun `onViewCreated should return failed because category detail throws an exception`() {
        onCategoryDetail_thenThrows()

        viewModel.onViewCreated()

        Assert.assertTrue(viewModel.visitableListLiveData.value == null)
    }

    @Test
    fun `the execution of onViewCreated should not be continued because warehouse id is zero`() {
        setAddressData(
            warehouseId = NO_WAREHOUSE_ID,
            shopId = shopId
        )

        viewModel.onViewCreated()

        viewModel.visitableListLiveData
            .verifyValueEquals(null)
        viewModel.outOfCoverageState
            .verifyValueEquals(Unit)
    }
}

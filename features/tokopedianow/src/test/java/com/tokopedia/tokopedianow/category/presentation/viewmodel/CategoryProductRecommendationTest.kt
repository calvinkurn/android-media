package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToTicker
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class CategoryProductRecommendationTest : TokoNowCategoryViewModelTestFixture() {
    @Test
    fun `after getFirstPage, call removeProductRecommendation to remove the recommendation in the layout`() {
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
                tickerList = tickerDataList.second
            )
        val hasBlockedAddToCart = tickerDataList.first

        // map title
        val titleUiModel = categoryDetailResponse
            .mapToCategoryTitle()

        // map category navigation
        val categoryNavigationUiModel = categoryDetailResponse
            .mapToCategoryNavigation()

        val resultList = mutableListOf(
            headerSpaceUiModel,
            chooseAddressUiModel,
            tickerUiModel,
            titleUiModel,
            categoryNavigationUiModel
        )

        val categoryNavigationList = categoryNavigationUiModel.categoryListUiModel.toMutableList()
        mapShowcaseProduct(
            hasAdded = true,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        viewModel.removeProductRecommendation()

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.visitableListLiveData
            .verifyValueEquals(resultList)
    }

    @Test
    fun `modify layout while its value is null should make removeProductRecommendation error and do nothing`() {
        val privateFieldNameLayout = "layout"

        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = null
        )

        viewModel.removeProductRecommendation()

        viewModel.visitableListLiveData
            .verifyValueEquals(null)
    }
}

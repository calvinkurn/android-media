package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryHeader
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

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

        val resultList = mutableListOf(
            header,
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
        val privateFieldNameLayout = "visitableList"

        viewModel.mockSuperClassField(
            name = privateFieldNameLayout,
            value = null
        )

        viewModel.removeProductRecommendation()

        viewModel.visitableListLiveData
            .verifyValueEquals(null)
    }
}

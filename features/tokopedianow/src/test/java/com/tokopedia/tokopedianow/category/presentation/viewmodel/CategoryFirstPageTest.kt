package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryHeader
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.category.presentation.model.CategoryOpenScreenTrackerModel
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class CategoryFirstPageTest : TokoNowCategoryViewModelTestFixture() {

    @Test
    fun `getFirstPage should return result with first 3 showcases for the first page of pagination`() {
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
        mapShowcaseProduct(
            hasAdded = true,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.openScreenTracker
            .verifyValueEquals(
                CategoryOpenScreenTrackerModel(
                    id = categoryDetailResponse.categoryDetail.data.id,
                    name = categoryDetailResponse.categoryDetail.data.name,
                    url = categoryDetailResponse.categoryDetail.data.url
                )
            )
        viewModel.visitableListLiveData
            .verifyValueEquals(resultList)
    }

    @Test
    fun `getFirstPage should return result even though one of showcases failed to be fetched`() {
        val isLoggedIn = false
        val userId = "12223"
        val deviceId = "11111"
        val expectedCategoryIdL2Failed = categoryProductResponseMap.keys.elementAt(1)

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
        onCategoryProduct_thenThrows(
            expectedCategoryIdL2Failed = expectedCategoryIdL2Failed
        )

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
        mapShowcaseProduct(
            expectedCategoryIdL2Failed = expectedCategoryIdL2Failed,
            hasAdded = true,
            categoryNavigationList = categoryNavigationList,
            resultList = resultList,
            hasBlockedAddToCart = hasBlockedAddToCart
        )

        verifyCategoryDetail()
        verifyTargetedTicker()
        viewModel.openScreenTracker
            .verifyValueEquals(
                CategoryOpenScreenTrackerModel(
                    id = categoryDetailResponse.categoryDetail.data.id,
                    name = categoryDetailResponse.categoryDetail.data.name,
                    url = categoryDetailResponse.categoryDetail.data.url
                )
            )
        viewModel.visitableListLiveData
            .verifyValueEquals(resultList)
    }
}

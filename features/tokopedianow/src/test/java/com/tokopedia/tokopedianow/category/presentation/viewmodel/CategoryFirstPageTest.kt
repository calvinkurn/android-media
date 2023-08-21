package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryDetailMapper.mapToTicker
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.ProductRecommendationMapper
import com.tokopedia.tokopedianow.category.presentation.model.CategoryOpenScreenTrackerModel
import com.tokopedia.tokopedianow.common.domain.mapper.TickerMapper
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

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
        viewModel.getFirstPage()

        // map header space
        val headerSpaceUiModel = categoryDetailResponse
            .mapToHeaderSpace(
                space = navToolbarHeight
            )

        // map choose address
        val chooseAddressUiModel = categoryDetailResponse
            .mapToChooseAddress(addressData)

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
            uniqueId = getUniqueId(
                isLoggedIn = isLoggedIn,
                userId = userId,
                deviceId = deviceId
            ),
            expectedCategoryIdL2Failed = expectedCategoryIdL2Failed
        )

        viewModel.onViewCreated()
        viewModel.getFirstPage()

        // map header space
        val headerSpaceUiModel = categoryDetailResponse
            .mapToHeaderSpace(
                space = navToolbarHeight
            )

        // map choose address
        val chooseAddressUiModel = categoryDetailResponse
            .mapToChooseAddress(addressData)

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

    @Test
    fun `modify layout while its value is null should make getFirstPage error and do nothing`() {
        val privateFieldNameLayout = "layout"

        viewModel.mockPrivateField(
            name = privateFieldNameLayout,
            value = null
        )

        viewModel.getFirstPage()

        viewModel.visitableListLiveData
            .verifyValueEquals(null)
    }
}

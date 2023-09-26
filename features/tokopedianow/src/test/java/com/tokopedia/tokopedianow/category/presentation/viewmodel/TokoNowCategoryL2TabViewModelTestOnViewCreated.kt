package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryStaticLayoutId
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateDivider
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.category.presentation.util.CategoryL2QuickFilterMapper
import com.tokopedia.tokopedianow.category.presentation.util.CategoryL2TabMapper.addProductCardItems
import com.tokopedia.tokopedianow.category.presentation.util.CategoryMenuMapper
import com.tokopedia.tokopedianow.category.presentation.util.TickerMapper
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.util.ProductAdsMapper
import com.tokopedia.tokopedianow.oldcategory.utils.TOKONOW_CATEGORY_L2
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class TokoNowCategoryL2TabViewModelTestOnViewCreated : TokoNowCategoryL2TabViewModelTestFixture() {

    @Test
    fun `given product list not empty when call onViewCrated should update visitable list with first page items`() {
        onGetProduct_thenReturn(getProductResponse)
        onGetProductAds_thenReturn(getProductAdsResponse)

        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)
        onGetTicker_thenReturn(warehouseId, getTargetedTickerResponse)

        val tickerData = TickerMapper.mapTickerData(getTargetedTickerResponse)
        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            tickerData = tickerData,
            componentList = componentList
        )

        viewModel.onViewCreated(data)

        val quickFilterComponent = getCategoryLayoutResponse
            .components.first { it.type == "product-list-filter" }
        val featuredProductComponent = getCategoryLayoutResponse
            .components.first { it.type == "featured-product" }

        val ticker = TokoNowTickerUiModel(
            id = "ticker_widget",
            tickers = tickerData.tickerList,
            hasOutOfStockTicker = false
        )

        val quickFilter = CategoryL2QuickFilterMapper.mapQuickFilter(
            id = quickFilterComponent.id,
            quickFilterResponse = getQuickFilterResponse,
            categoryFilterResponse = getCategoryFilterResponse,
            filterController = filterController
        )

        val productAdsCarousel = ProductAdsMapper.mapProductAdsCarousel(
            id = featuredProductComponent.id,
            response = getProductAdsResponse,
            miniCartData = MiniCartSimplifiedData(),
            hasBlockedAddToCart = false
        )

        val expectedVisitableList = mutableListOf(
            ticker,
            quickFilter,
            productAdsCarousel
        )

        expectedVisitableList.addProductCardItems(
            response = getProductResponse,
            miniCartData = MiniCartSimplifiedData(),
            hasBlockedAddToCart = false
        )

        verifyRequestQueryParams()

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `given product list empty when call onViewCrated should update visitable list with empty state items`() {
        val searchData = getProductResponse.searchProduct.data.copy(productList = emptyList())
        val searchProduct = getProductResponse.searchProduct.copy(data = searchData)
        val emptyProductResponse = getProductResponse.copy(searchProduct)
        val violation = searchData.violation

        onGetProduct_thenReturn(emptyProductResponse)
        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)
        onGetCategoryList_thenReturn(getCategoryListResponse)

        val tickerData = TickerMapper.mapTickerData(getTargetedTickerResponse)
        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            tickerData = tickerData,
            componentList = componentList
        )

        viewModel.onViewCreated(data)

        val seeAllAppLink =
            "tokopedia-android-internal://now/see-all-category?warehouse_id=$warehouseId"

        val quickFilter = CategoryL2QuickFilterMapper.mapQuickFilter(
            id = CategoryStaticLayoutId.QUICK_FILTER,
            quickFilterResponse = getQuickFilterResponse,
            categoryFilterResponse = getCategoryFilterResponse,
            filterController = filterController
        )

        val emptyState = TokoNowEmptyStateNoResultUiModel(
            activeFilterList = emptyList(),
            excludeFilter = null,
            defaultTitle = violation.headerText,
            defaultDescription = violation.descriptionText,
            defaultImage = violation.imageUrl,
            enablePrimaryButton = false,
            enableSecondaryButton = false
        )

        val categoryList = CategoryMenuMapper.mapToCategoryList(
            response = getCategoryListResponse.data,
            seeAllAppLink = seeAllAppLink
        )

        val categoryMenu = TokoNowCategoryMenuUiModel(
            id = CategoryStaticLayoutId.CATEGORY_MENU_EMPTY_STATE,
            title = "",
            categoryListUiModel = categoryList,
            state = TokoNowLayoutState.SHOW,
            source = TOKONOW_CATEGORY_L2,
            seeAllAppLink = seeAllAppLink
        )

        val divider = CategoryEmptyStateDivider()

        val productRecommendation = TokoNowProductRecommendationUiModel(
            requestParam = GetRecommendationRequestParam(
                pageName = RecomPageConstant.TOKONOW_NO_RESULT,
                categoryIds = emptyList(),
                xSource = RecomPageConstant.RECOM_WIDGET,
                isTokonow = true,
                pageNumber = RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET,
                keywords = emptyList(),
                xDevice = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
            ),
            tickerPageSource = GetTargetedTickerUseCase.CATEGORY_PAGE
        )

        val feedbackWidget = TokoNowFeedbackWidgetUiModel(isDivider = true)

        val expectedVisitableList = mutableListOf(
            quickFilter,
            emptyState,
            categoryMenu,
            divider,
            productRecommendation
        )

        verifySortFilterQueryParams()

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

    private fun verifyRequestQueryParams() {
        val expectedGetProductQueryParams = createGetProductQueryParams(categoryIdL1, categoryIdL2)
        val expectedGetProductAdsQueryParams = createGetProductAdsParams(categoryIdL2)

        verifyGetProductUseCaseCalled(expectedGetProductQueryParams)
        verifyGetProductAdsUseCaseCalled(expectedGetProductAdsQueryParams)
        verifySortFilterQueryParams()
    }

    private fun verifySortFilterQueryParams() {
        val expectedGetQuickFilterQueryParams = createRequestQueryParams(
            source = "quick_filter_tokonow_directory"
        )

        val expectedGetCategoryFilterQueryParams = createGetCategoryFilterQueryParams()

        verifyGetSortFilterUseCaseCalled(expectedGetQuickFilterQueryParams)
        verifyGetSortFilterUseCaseCalled(expectedGetCategoryFilterQueryParams)
    }
}

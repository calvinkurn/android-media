package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.category.mapper.CategoryL2QuickFilterMapper
import com.tokopedia.tokopedianow.category.mapper.CategoryL2TabMapper.addProductCardItems
import com.tokopedia.tokopedianow.category.mapper.CategoryMenuMapper
import com.tokopedia.tokopedianow.category.mapper.TickerMapper
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TypeFactory
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryStaticLayoutId
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryDividerUiModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.util.ProductAdsMapper
import com.tokopedia.tokopedianow.category.constant.TOKONOW_CATEGORY_L2
import com.tokopedia.tokopedianow.searchcategory.domain.model.GetFeedbackFieldModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TokoNowFeedbackWidgetUiModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoNowCategoryL2TabViewModelTestOnViewCreated : TokoNowCategoryL2TabViewModelTestFixture() {

    @Test
    fun `given product list not empty when call onViewCrated should update visitable list with first page items`() {
        val getTargetedTickerOosResponse = crateGetTargetedTickerOutOfStockResponse()

        onGetProductList(thenReturn = getProductResponse)
        onGetProductAds(thenReturn = getProductAdsResponse)

        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)
        onGetTicker(thenReturn = getTargetedTickerOosResponse)

        val tickerData = TickerMapper.mapTickerData(getTargetedTickerOosResponse)
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
            tickers = tickerData.oosTickerList,
            hasOutOfStockTicker = true
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

        verifyVisitableList(expectedVisitableList)
    }

    @Test
    fun `given product ads empty when call onViewCrated should remove ads carousel from visitableList`() {
        val emptyProductAdsResponse = getProductAdsResponse
            .copy(productList = emptyList())
        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            componentList = componentList
        )

        onGetProductList(thenReturn = getProductResponse)
        onGetProductAds(thenReturn = emptyProductAdsResponse)

        viewModel.onViewCreated(data)

        val actualProductAdsCarousel = viewModel.visitableListLiveData.value.orEmpty()
            .firstOrNull { it is TokoNowAdsCarouselUiModel }

        assertEquals(null, actualProductAdsCarousel)
    }

    @Test
    fun `given get product ads throws error when call onViewCrated should remove ads carousel from visitableList`() {
        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            componentList = componentList
        )

        onGetProductList(thenReturn = getProductResponse)
        onGetProductAds_throwsError()

        viewModel.onViewCreated(data)

        val actualProductAdsCarousel = viewModel.visitableListLiveData.value.orEmpty()
            .firstOrNull { it is TokoNowAdsCarouselUiModel }

        assertEquals(null, actualProductAdsCarousel)
    }

    @Test
    fun `given product list empty when call onViewCrated should update visitable list with empty state items`() {
        val searchData = getProductResponse.searchProduct.data.copy(productList = emptyList())
        val searchProduct = getProductResponse.searchProduct.copy(data = searchData)
        val emptyProductResponse = getProductResponse.copy(searchProduct)
        val violation = searchData.violation

        val getFeedbackToggleResponse = GetFeedbackFieldModel.Data(isActive = true)

        onGetProductList(thenReturn = emptyProductResponse)
        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)
        onGetCategoryList_thenReturn(getCategoryListResponse)
        onGetFeedbackToggle_thenReturn(getFeedbackToggleResponse)

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
            activeFilterList = null,
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

        val divider = CategoryDividerUiModel()

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
            productRecommendation,
            feedbackWidget
        )

        verifySortFilterQueryParams()

        verifyVisitableList(expectedVisitableList)
    }

    @Test
    fun `given feedback toggle isActive false when show empty sate should not add feedback loop widget to visitableList`() {
        val searchData = getProductResponse.searchProduct.data.copy(productList = emptyList())
        val searchProduct = getProductResponse.searchProduct.copy(data = searchData)
        val emptyProductResponse = getProductResponse.copy(searchProduct)
        val violation = searchData.violation

        val getFeedbackToggleResponse = GetFeedbackFieldModel.Data(isActive = false)

        onGetProductList(thenReturn = emptyProductResponse)
        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)
        onGetCategoryList_thenReturn(getCategoryListResponse)
        onGetFeedbackToggle_thenReturn(getFeedbackToggleResponse)

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
            activeFilterList = null,
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

        val divider = CategoryDividerUiModel()

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

        val expectedVisitableList = mutableListOf(
            quickFilter,
            emptyState,
            categoryMenu,
            divider,
            productRecommendation
        )

        verifySortFilterQueryParams()

        verifyVisitableList(expectedVisitableList)
    }

    @Test
    fun `given visitableList null when show empty state should not crash`() {
        setVisitableList(null)

        val searchData = getProductResponse.searchProduct.data.copy(productList = emptyList())
        val searchProduct = getProductResponse.searchProduct.copy(data = searchData)
        val emptyProductResponse = getProductResponse.copy(searchProduct)

        onGetProductList(thenReturn = emptyProductResponse)
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

        viewModel.visitableListLiveData
            .verifyValueEquals(null)
    }

    @Test
    fun `given get userId throws error when empty state should NOT call get mini cart use case`() {
        val searchData = getProductResponse.searchProduct.data.copy(productList = emptyList())
        val searchProduct = getProductResponse.searchProduct.copy(data = searchData)
        val emptyProductResponse = getProductResponse.copy(searchProduct)

        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            componentList = componentList
        )

        onGetShopId_throwsError()
        onGetIsUserLoggedIn_thenReturn(isLoggedIn = true)
        onGetProductList(thenReturn = emptyProductResponse)

        viewModel.onViewCreated(data)

        verifyGetMiniCartUseCaseNotCalled()
    }

    @Test
    fun `given unsupported layout when onViewCreated should remove unsupported layout from visitableList`() {
        val visitableList = mutableListOf(UnknownLayoutType)
        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            componentList = componentList
        )

        onGetProductList(thenReturn = getProductResponse)

        setVisitableList(visitableList)

        viewModel.onViewCreated(data)

        verifyUnknownLayoutTypeNotExists()
    }

    private fun verifyUnknownLayoutTypeNotExists() {
        val actualUnknownLayoutTypeItem = viewModel.visitableListLiveData.value.orEmpty()
            .firstOrNull { it is UnknownLayoutType }

        assertEquals(null, actualUnknownLayoutTypeItem)
    }

    object UnknownLayoutType: Visitable<CategoryL2TypeFactory> {
        override fun type(typeFactory: CategoryL2TypeFactory?): Int {
            return 0
        }
    }
}

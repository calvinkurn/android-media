package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.category.mapper.CategoryL2TabMapper.addProductCardItems
import com.tokopedia.tokopedianow.category.mapper.CategoryMenuMapper
import com.tokopedia.tokopedianow.category.presentation.constant.CategoryStaticLayoutId
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryDividerUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.util.ProductAdsMapper
import com.tokopedia.tokopedianow.category.constant.TOKONOW_CATEGORY_L2
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoNowCategoryL2TabViewModelTestFilter : TokoNowCategoryL2TabViewModelTestFixture() {

    @Test
    fun `given quick filter product result empty when applyQuickFilter should update visitable list with empty state items`() {
        onGetProductList(thenReturn = getProductResponse)
        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)
        onGetCategoryList_thenReturn(getCategoryListResponse)

        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            tickerData = GetTickerData(),
            componentList = componentList
        )

        viewModel.onViewCreated(data)

        val searchData = getProductResponse.searchProduct.data.copy(productList = emptyList())
        val searchProduct = getProductResponse.searchProduct.copy(data = searchData)
        val emptyProductResponse = getProductResponse.copy(searchProduct)
        val violation = searchData.violation

        val quickFilter = viewModel.visitableListLiveData.value.orEmpty()
            .filterIsInstance<CategoryQuickFilterUiModel>().first()

        val filter = quickFilter.itemList[1].filter
        val option = filter.options.first()

        val getProductQueryParamsWithFilter =
            createGetProductQueryParams(categoryIdL1, categoryIdL2)
        getProductQueryParamsWithFilter[option.key] = option.value

        onGetProductList(
            withQueryParams = getProductQueryParamsWithFilter,
            thenReturn = emptyProductResponse
        )

        viewModel.applyQuickFilter(filter, option)

        val seeAllAppLink =
            "tokopedia-android-internal://now/see-all-category?warehouse_id=$warehouseId"

        val excludedFilter = Option(
            name = "Semua",
            key = "exclude_sc",
            value = "4860",
            inputType = "radio",
            totalData = "70",
            iconUrl = "https://images.tokopedia.net/img/WgKiGm/2021/7/7/e9e12971-6999-422d-9543-3a10e01812c7.png"
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

        verifyGetProductUseCaseCalled(
            queryParams = getProductQueryParamsWithFilter
        )

        verifyVisitableList(expectedVisitableList)
    }

//    @Test
    fun `given filter product result NOT empty when applySortFilter should update visitable list with product items`() {
        onGetProductList(thenReturn = getProductResponse)
        onGetProductAds(thenReturn = getProductAdsResponse)
        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)

        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            tickerData = GetTickerData(),
            componentList = componentList
        )

        viewModel.onViewCreated(data)

        val quickFilter = viewModel.visitableListLiveData.value.orEmpty()
            .filterIsInstance<CategoryQuickFilterUiModel>().first()

        val filter = quickFilter.itemList.first().filter
        val option = filter.options.first()

        val sortFilter = ApplySortFilterModel(
            mapParameter = mapOf(option.key to option.value),
            selectedFilterMapParameter = emptyMap(),
            selectedSortMapParameter = emptyMap(),
            selectedSortName = option.name,
            sortAutoFilterMapParameter = emptyMap()
        )

        val getProductQueryParamsWithFilter =
            createGetProductQueryParams(categoryIdL1, categoryIdL2)
        val getProductAdsParamsWithFilter = createGetProductAdsParams(categoryIdL2)

        getProductQueryParamsWithFilter[option.key] = option.value
        getProductAdsParamsWithFilter[option.key] = option.value

        onGetProductList(
            withQueryParams = getProductQueryParamsWithFilter,
            thenReturn = getProductResponse
        )

        onGetProductAds(
            withQueryParams = getProductAdsParamsWithFilter,
            thenReturn = getProductAdsResponse
        )

        viewModel.applySortFilter(sortFilter)

        val featuredProductComponent = getCategoryLayoutResponse
            .components.first { it.type == "featured-product" }

        val productAdsCarousel = ProductAdsMapper.mapProductAdsCarousel(
            id = featuredProductComponent.id,
            response = getProductAdsResponse,
            miniCartData = MiniCartSimplifiedData(),
            hasBlockedAddToCart = false
        )

        val expectedVisitableList = mutableListOf<Visitable<*>>(
            quickFilter,
            productAdsCarousel
        )

        expectedVisitableList.addProductCardItems(
            response = getProductResponse,
            miniCartData = MiniCartSimplifiedData(),
            hasBlockedAddToCart = false
        )

        verifySortFilterQueryParams()

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

//    @Test
    fun `given filter product result NOT empty when applyFilterFromCategoryChooser should update visitable list with product items`() {
        onGetProductList(thenReturn = getProductResponse)
        onGetProductAds(thenReturn = getProductAdsResponse)
        onGetQuickFilter_thenReturn(getQuickFilterResponse)
        onGetCategoryFilter_thenReturn(getCategoryFilterResponse)

        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            tickerData = GetTickerData(),
            componentList = componentList
        )

        viewModel.onViewCreated(data)

        val quickFilter = viewModel.visitableListLiveData.value.orEmpty()
            .filterIsInstance<CategoryQuickFilterUiModel>().first()

        val filter = quickFilter.itemList.first().filter
        val option = filter.options.first()

        val getProductQueryParamsWithFilter =
            createGetProductQueryParams(categoryIdL1, categoryIdL2)
        val getProductAdsParamsWithFilter = createGetProductAdsParams(categoryIdL2)

        getProductQueryParamsWithFilter[option.key] = option.value
        getProductAdsParamsWithFilter[option.key] = option.value

        onGetProductList(
            withQueryParams = getProductQueryParamsWithFilter,
            thenReturn = getProductResponse
        )

        onGetProductAds(
            withQueryParams = getProductAdsParamsWithFilter,
            thenReturn = getProductAdsResponse
        )

        viewModel.applyFilterFromCategoryChooser(option)

        val featuredProductComponent = getCategoryLayoutResponse
            .components.first { it.type == "featured-product" }

        val productAdsCarousel = ProductAdsMapper.mapProductAdsCarousel(
            id = featuredProductComponent.id,
            response = getProductAdsResponse,
            miniCartData = MiniCartSimplifiedData(),
            hasBlockedAddToCart = false
        )

        val expectedVisitableList = mutableListOf<Visitable<*>>(
            quickFilter,
            productAdsCarousel
        )

        expectedVisitableList.addProductCardItems(
            response = getProductResponse,
            miniCartData = MiniCartSimplifiedData(),
            hasBlockedAddToCart = false
        )

        verifySortFilterQueryParams()

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `given onDismissFilterBottomSheet when onOpenFilterPage should call getSortFilterUseCase once and update dynamicFilterModelLiveData`() {
        val queryParams = createRequestQueryParams()

        onGetSortFilterFilter(
            withQueryParams = queryParams,
            thenReturn = getCategoryFilterResponse
        )

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)

        viewModel.onOpenFilterPage()

        viewModel.onDismissFilterBottomSheet()

        viewModel.onOpenFilterPage()
        viewModel.onOpenFilterPage()

        viewModel.dynamicFilterModelLiveData
            .verifyValueEquals(getCategoryFilterResponse)

        verifyGetSortFilterUseCaseCalled(queryParams = queryParams, times = 1)
    }

    @Test
    fun `given getSortFilterUseCase when onOpenFilterPage should set filterBottomSheetOpened false`() {
        onGetSortFilterFilter_throwsError()

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)
        viewModel.onOpenFilterPage()

        verifyGetSortFilterUseCaseCalled(times = 1)
    }

    @Test
    fun `given applyQuickFilter when onRemoveFilter should call get product use case without selected filter option params`() {
        val queryParams = createGetProductQueryParams(categoryIdL1, categoryIdL2)
        val pMinOption = Option(key = "pmin", value = "2000")
        val pMaxOption = Option(key = "pmax", value = "2000")
        val filter = Filter(options = listOf(pMinOption, pMaxOption))

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)

        viewModel.applyQuickFilter(filter, pMinOption)
        viewModel.applyQuickFilter(filter, pMaxOption)

        viewModel.onRemoveFilter(pMinOption)
        viewModel.onRemoveFilter(pMaxOption)

        verifyGetProductUseCaseCalled(queryParams)
    }

    @Test
    fun `when getProductCount should update filterProductCountLiveData with product count`() {
        val option = Option(key = "pmin", value = "2000")

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)

        val mapParameter = mapOf(option.key to option.value)
        val requestParams = createGetProductCountRequestParams(mapParameter)

        onGetProductCount_thenReturn("2")

        viewModel.getProductCount(option)

        verifyGetProductCountUseCaseCalled(requestParams)

        viewModel.filterProductCountLiveData
            .verifyValueEquals("2")
    }

    @Test
    fun `when getProductCount failed should not update filterProductCountLiveData`() {
        val option = Option(key = "pmin", value = "2000")

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)

        onGetProductCount_throwsError()

        viewModel.getProductCount(option)

        viewModel.filterProductCountLiveData
            .verifyValueEquals("")
    }

    @Test
    fun `given applySortFilter when getMapParameter should return mapParameter with filter option parameters`() {
        val option = Option(key = "pmin", value = "2000")
        val mapParameter = mapOf(option.key to option.value)

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        val sortFilter = ApplySortFilterModel(
            mapParameter = mapParameter,
            selectedFilterMapParameter = emptyMap(),
            selectedSortMapParameter = emptyMap(),
            selectedSortName = option.name,
            sortAutoFilterMapParameter = emptyMap()
        )

        viewModel.onViewCreated(data)
        viewModel.applySortFilter(sortFilter)

        val actualMapParameter = viewModel.getMapParameter()
        val expectedMapParameter = createExpectedMapParameter(mapParameter)

        assertEquals(expectedMapParameter, actualMapParameter)
    }

    @Test
    fun `given option has exclude prefix when applyQuickFilter should call get product use case without exclude sc filter param`() {
        val option = Option(key = "exclude_sc", value = "1257")
        val filter = Filter(options = listOf(option))

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)
        viewModel.applyQuickFilter(filter, option)

        val queryParams = createGetProductQueryParams(categoryIdL1, categoryIdL2)

        verifyGetProductUseCaseCalled(queryParams)
    }

    @Test
    fun `given filter option when applyQuickFilter twice should call get product use case`() {
        val option = Option(key = "sc", value = "1257")
        val filter = Filter(options = listOf(option))

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)
        viewModel.applyQuickFilter(filter, option)
        viewModel.applyQuickFilter(filter, option)

        val queryParams = createGetProductQueryParams(categoryIdL1, categoryIdL2)
        queryParams["sc"] = "1257"

        verifyGetProductUseCaseCalled(queryParams)
    }

    @Test
    fun `given option has exclude prefix when onRemoveFilter should call get product use case without exclude sc filter param`() {
        val option = Option(key = "exclude_sc", value = "1257")

        val data = CategoryL2TabData(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)
        viewModel.onRemoveFilter(option)

        val queryParams = createGetProductQueryParams(categoryIdL1, categoryIdL2)

        verifyGetProductUseCaseCalled(queryParams)
    }

    private fun createExpectedMapParameter(queryParams: Map<String, String>): MutableMap<String, String> {
        return mutableMapOf<String, String>().apply {
            put("ob", "23")
            put("navsource", "tokonow_directory")
            put("source", "tokonow_directory")
            put("srp_page_id", categoryIdL1)
            putAll(queryParams)
        }
    }
}

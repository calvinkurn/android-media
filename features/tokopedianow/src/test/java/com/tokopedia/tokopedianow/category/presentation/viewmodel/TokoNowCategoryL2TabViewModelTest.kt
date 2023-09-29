package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.mapper.TickerMapper
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class TokoNowCategoryL2TabViewModelTest: TokoNowCategoryL2TabViewModelTestFixture() {

    @Test
    fun `given empty state when removeProductRecommendationWidget remove recommendation widget from visitable list`() {
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
        viewModel.removeProductRecommendationWidget()

        viewModel.visitableListLiveData
            .verifyRecommendationWidgetNotExists()
    }

    @Test
    fun `given categoryId L1 and L2 when getCategoryIdForTracking should return categoryId L1 and L2 for tracking`() {
        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)

        val actualCategoryId = viewModel.getCategoryIdForTracking()
        val expectedCategoryId = "$categoryIdL1/$categoryIdL2"

        assertEquals(expectedCategoryId, actualCategoryId)
    }

    @Test
    fun `given categoryIdL2 is empty when getCategoryIdForTracking should return categoryIdL1 for tracking`() {
        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = ""
        )

        viewModel.onViewCreated(data)

        val actualCategoryId = viewModel.getCategoryIdForTracking()
        val expectedCategoryId = categoryIdL1

        assertEquals(expectedCategoryId, actualCategoryId)
    }

    @Test
    fun `given appLink empty when routeToProductDetailPage should update routeAppLinkLiveData with affiliate applink`() {
        val uri = "tokopedia-android-internal://marketplace/product-detail/9991/"
        val affiliateLink = "tokopedia-android-internal://marketplace/product-detail/9991?aff_uuid=1523"

        onCreateAffiliateLink_thenReturn(affiliateLink)

        viewModel.routeToProductDetailPage("9991")

        verifyCreateAffiliateLinkCalled(withUri = uri)

        viewModel.routeAppLinkLiveData
            .verifyValueEquals(affiliateLink)
    }

    @Test
    fun `given appLink when routeToProductDetailPage should update routeAppLinkLiveData with affiliate applink`() {
        val appLink = "tokopedia-android-internal://marketplace/atc-variant/2131/512/"
        val affiliateLink = "tokopedia-android-internal://marketplace/product-detail/9991?aff_uuid=1523"

        onCreateAffiliateLink_thenReturn(affiliateLink)

        viewModel.routeToProductDetailPage("9991", appLink)

        verifyCreateAffiliateLinkCalled(withUri = appLink)

        viewModel.routeAppLinkLiveData
            .verifyValueEquals(affiliateLink)
    }

    @Test
    fun `when updateWishlistStatus should update product wishlist status and tracker live data`() {
        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            componentList = componentList
        )

        onGetProductList(thenReturn = getProductResponse)

        viewModel.onViewCreated(data)

        val productItem = viewModel.visitableListLiveData.value.orEmpty()
            .filterIsInstance<ProductItemDataView>()[2]

        val product = productItem.productCardModel
        val productId = product.productId

        viewModel.updateWishlistStatus(productId, true)

        val expectedWishlistProduct = productItem.copy(
            productCardModel = product.copy(hasBeenWishlist = true))

        val actualWishlistProduct = viewModel.visitableListLiveData.value.orEmpty()
            .filterIsInstance<ProductItemDataView>()[2]

        assertEquals(expectedWishlistProduct, actualWishlistProduct)

        viewModel.clickWishlistTracker
            .verifyValueEquals(Pair(2, productId))
    }

    @Test
    fun `given empty product list when getCategoryMenuData throws error should mapCategoryMenuData error state`() {
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

        onGetProductList(thenReturn = emptyProductResponse)
        onGetCategoryList_thenReturn(getCategoryListResponse)

        viewModel.onViewCreated(data)

        onGetCategoryList_throwsError()

        viewModel.getCategoryMenuData()

        val visitableList = viewModel.visitableListLiveData.value.orEmpty()

        val actualCategoryMenu = visitableList
            .filterIsInstance<TokoNowCategoryMenuUiModel>()
            .first()

        val expectedCategoryMenu = actualCategoryMenu.copy(
            categoryListUiModel = null,
            state = TokoNowLayoutState.HIDE,
            seeAllAppLink = ""
        )

        assertEquals(expectedCategoryMenu, actualCategoryMenu)
    }

    @Test
    fun `given getWarehouseId throws exception when getCategoryMenuData should NOT call get category list use case`() {
        onGetWarehouseId_throwsError()

        viewModel.getCategoryMenuData()

        verifyGetCategoryListUseCaseNotCalled()
    }

    @Test
    fun `when getWarehouseIds should return warehouseIds string`() {
        onGetAddressData_thenReturn(localCacheModel)

        val expectedWarehouseIds = "151245,151246"
        val actualWarehouseIds = viewModel.getWarehouseIds()

        assertEquals(expectedWarehouseIds, actualWarehouseIds)
    }

    @Test
    fun `given product list when onClickSimilarProduct should update tracker live data`() {
        val productId = "6903919751"
        val componentList = getCategoryLayoutResponse.components

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            componentList = componentList
        )

        onGetProductList(thenReturn = getProductResponse)

        viewModel.onViewCreated(data)

        viewModel.onClickSimilarProduct(productId)

        viewModel.clickSimilarProductTracker
            .verifyValueEquals(Pair(2, productId))
    }

    @Test
    fun `given user isLoggedIn true when onResume should call get mini cart use case`() {
        onGetIsUserLoggedIn_thenReturn(isLoggedIn = true)

        viewModel.onResume()

        verifyGetMiniCartUseCaseCalled()
    }

    private fun LiveData<List<Visitable<*>>>.verifyRecommendationWidgetNotExists() {
        val visitableList = value.orEmpty()
        val recommendationWidget = visitableList.firstOrNull {
            it is TokoNowProductRecommendationUiModel
        }
        assertEquals(null, recommendationWidget)
    }
}

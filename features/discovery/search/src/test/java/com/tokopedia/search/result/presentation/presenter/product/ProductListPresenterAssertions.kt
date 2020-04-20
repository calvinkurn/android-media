@file:Suppress("unused")

package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.presenter.product.testinstance.searchProductModelCommon
import com.tokopedia.search.shouldBe
import io.mockk.MockKVerificationScope

fun MockKVerificationScope.verifyShowLoading(productListView: ProductListSectionContract.View) {
    productListView.stopPreparePagePerformanceMonitoring()
    productListView.startNetworkRequestPerformanceMonitoring()

    productListView.showRefreshLayout()
}

fun MockKVerificationScope.verifyProcessingData(productListView: ProductListSectionContract.View) {
    productListView.stopNetworkRequestPerformanceMonitoring()
    productListView.startRenderPerformanceMonitoring()

    productListView.isLandingPage

    productListView.clearLastProductItemPositionFromCache()
    productListView.lastProductItemPositionFromCache
    productListView.saveLastProductItemPositionToCache(any())

    productListView.setAutocompleteApplink(searchProductModelCommon.searchProduct.autocompleteApplink)
    productListView.setDefaultLayoutType(searchProductModelCommon.searchProduct.defaultView)
    productListView.removeLoading()
    productListView.setProductList(any())
    productListView.showFreeOngkirShowCase(false)
    productListView.initQuickFilter(any())
    productListView.addLoading()
    productListView.setTotalSearchResultCount(any())
    productListView.stopTracePerformanceMonitoring()
}

fun MockKVerificationScope.verifyHideLoading(productListView: ProductListSectionContract.View) {
    productListView.hideRefreshLayout()
}

fun MockKVerificationScope.verifyShowError(productListView: ProductListSectionContract.View) {
    productListView.showRefreshLayout()
    productListView.removeLoading()
    productListView.showNetworkError(any())
    productListView.hideRefreshLayout()
}

fun MockKVerificationScope.verifyShowLoadMoreError(productListView: ProductListSectionContract.View, startRow: Int = 0) {
    productListView.removeLoading()
    productListView.hideRefreshLayout()
    productListView.showNetworkError(startRow)
}

fun MockKVerificationScope.verifySendTrackingOnFirstTimeLoad(productListView: ProductListSectionContract.View) {
    productListView.sendTrackingEventAppsFlyerViewListingSearch(any(), any(), any())
    productListView.sendTrackingEventMoEngageSearchAttempt(any(), any(), any())
    productListView.previousKeyword
    productListView.sendTrackingGTMEventSearchAttempt(any())
}

fun MockKVerificationScope.verifyProcessingNextPage(productListView: ProductListSectionContract.View) {
    productListView.lastProductItemPositionFromCache
    productListView.saveLastProductItemPositionToCache(8)
    productListView.removeLoading()
    productListView.addProductList(any())
    productListView.addLoading()
    productListView.updateScrollListener()

    productListView.hideRefreshLayout()
}

fun MockKVerificationScope.verifyIsVisible(productListView: ProductListSectionContract.View) {
    productListView.setupSearchNavigation()
    productListView.trackScreenAuthenticated()
}

fun MockKVerificationScope.verifyIsAdded(productListView: ProductListSectionContract.View) {
    productListView.reloadData()
}

internal fun WishlistTrackingModel.assert(
        expectedWishlistTrackingModel: WishlistTrackingModel
) {

    isAddWishlist.shouldBe(expectedWishlistTrackingModel.isAddWishlist,
            "Wishlist tracking model isAddWishlist should be ${expectedWishlistTrackingModel.isAddWishlist}")

    productId shouldBe expectedWishlistTrackingModel.productId
    isTopAds.shouldBe(expectedWishlistTrackingModel.isTopAds,
            "Wishlist tracking model isTopAds should be ${expectedWishlistTrackingModel.isTopAds}")
    isUserLoggedIn.shouldBe(expectedWishlistTrackingModel.isUserLoggedIn,
            "Wishlist tracking model isUserLoggedIn should be ${expectedWishlistTrackingModel.isUserLoggedIn}")
    keyword shouldBe keyword
}
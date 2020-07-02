@file:Suppress("unused")

package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.shouldBe
import io.mockk.CapturingSlot
import io.mockk.MockKVerificationScope

fun MockKVerificationScope.verifyShowLoading(productListView: ProductListSectionContract.View) {
    productListView.stopPreparePagePerformanceMonitoring()
    productListView.startNetworkRequestPerformanceMonitoring()

    productListView.showRefreshLayout()
}

fun MockKVerificationScope.verifyProcessingData(
        productListView: ProductListSectionContract.View,
        searchProductModel: SearchProductModel,
        visitableListSlot: CapturingSlot<List<Visitable<*>>>
) {
    productListView.stopNetworkRequestPerformanceMonitoring()
    productListView.startRenderPerformanceMonitoring()

    productListView.isLandingPage

    productListView.clearLastProductItemPositionFromCache()
    productListView.lastProductItemPositionFromCache
    productListView.saveLastProductItemPositionToCache(any())

    productListView.setAutocompleteApplink(searchProductModel.searchProduct.data.autocompleteApplink)
    productListView.setDefaultLayoutType(searchProductModel.searchProduct.header.defaultView)
    productListView.removeLoading()
    productListView.setProductList(capture(visitableListSlot))
    productListView.backToTop()
    productListView.showFreeOngkirShowCase(true)
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
    productListView.queryKey
    productListView.sendTrackingEventAppsFlyerViewListingSearch(any(), any(), any())
    productListView.sendTrackingEventMoEngageSearchAttempt(any(), any(), any())
    productListView.previousKeyword
    productListView.sendTrackingGTMEventSearchAttempt(any())
}

fun MockKVerificationScope.verifyProcessingNextPage(productListView: ProductListSectionContract.View, visitableListSlot: CapturingSlot<List<Visitable<*>>>) {
    productListView.lastProductItemPositionFromCache
    productListView.saveLastProductItemPositionToCache(16)
    productListView.removeLoading()
    productListView.addProductList(capture(visitableListSlot))
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
@file:Suppress("unused")

package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.search.listShouldBe
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselProduct
import com.tokopedia.search.result.presentation.ProductListSectionContract
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
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
    productListView.addLoading()
    productListView.stopTracePerformanceMonitoring()
}

fun MockKVerificationScope.verifyHideLoading(productListView: ProductListSectionContract.View) {
    productListView.hideRefreshLayout()
}

fun MockKVerificationScope.verifyShowError(productListView: ProductListSectionContract.View) {
    productListView.showRefreshLayout()
    productListView.removeLoading()
    productListView.showNetworkError(any(), any())
    productListView.hideRefreshLayout()
}

fun MockKVerificationScope.verifyShowLoadMoreError(productListView: ProductListSectionContract.View, startRow: Int = 0) {
    productListView.removeLoading()
    productListView.hideRefreshLayout()
    productListView.showNetworkError(startRow, any())
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

internal fun List<InspirationCarouselDataView.Option.Product>.assert(
        expectedInspirationCarouselProduct: List<InspirationCarouselProduct>,
        inspirationCarouselType: String,
        inspirationCarouselLayout: String,
        optionPosition: Int,
        optionTitle: String,
) {
    var productPosition = 1
    listShouldBe(expectedInspirationCarouselProduct) { actualProduct, expectedProduct ->
        actualProduct.id shouldBe expectedProduct.id
        actualProduct.name shouldBe expectedProduct.name
        actualProduct.price shouldBe expectedProduct.price
        actualProduct.priceStr shouldBe expectedProduct.priceStr
        actualProduct.imgUrl shouldBe expectedProduct.imgUrl
        actualProduct.rating shouldBe expectedProduct.rating
        actualProduct.countReview shouldBe expectedProduct.countReview
        actualProduct.url shouldBe expectedProduct.url
        actualProduct.applink shouldBe expectedProduct.applink
        actualProduct.description shouldBe expectedProduct.description
        actualProduct.inspirationCarouselType shouldBe inspirationCarouselType
        actualProduct.ratingAverage shouldBe expectedProduct.ratingAverage
        actualProduct.labelGroupDataList.listShouldBe(expectedProduct.labelGroupList) { actualLabelGroup, expectedLabelGroup ->
            actualLabelGroup.title shouldBe expectedLabelGroup.title
            actualLabelGroup.position shouldBe expectedLabelGroup.position
            actualLabelGroup.type shouldBe expectedLabelGroup.type
            actualLabelGroup.imageUrl shouldBe expectedLabelGroup.url
        }
        actualProduct.layout shouldBe inspirationCarouselLayout
        actualProduct.originalPrice shouldBe expectedProduct.originalPrice
        actualProduct.discountPercentage shouldBe expectedProduct.discountPercentage
        actualProduct.optionPosition shouldBe optionPosition
        actualProduct.position shouldBe productPosition
        actualProduct.optionTitle shouldBe optionTitle
        productPosition++
    }
}
package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

internal class SearchProductHandleClickProductTest: ProductListPresenterTestFixtures() {

    private val position = 1
    private val capturedProductItemViewModel = slot<ProductItemViewModel>()

    @Test
    fun `Handle onProductClick with null ProductItemViewModel`() {
        `When handle product click`(null, -1)

        `Then verify view not doing anything`()
    }

    private fun `When handle product click`(productItemViewModel: ProductItemViewModel?, position: Int) {
        productListPresenter.onProductClick(productItemViewModel, position)
    }

    private fun `Then verify view not doing anything`() {
        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductClick for Top Ads Product`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Pixel 4"
            it.price = "Rp100.000.000"
            it.categoryID = 13
            it.isTopAds = true
            it.topadsImpressionUrl = topAdsImpressionUrl
            it.topadsClickUrl = topAdsClickUrl
            it.position = position
        }

        `When handle product click`(productItemViewModel, position)

        `Then verify view interaction for Top Ads Product`(productItemViewModel, position)
        `Then verify position is correct`(productItemViewModel)
    }

    private fun `Then verify view interaction for Top Ads Product`(productItemViewModel: ProductItemViewModel, position: Int) {
        verify {
            productListView.sendTopAdsTrackingUrl(productItemViewModel.topadsClickUrl)
            productListView.sendTopAdsGTMTrackingProductClick(capture(capturedProductItemViewModel))
            productListView.routeToProductDetail(productItemViewModel, position)
        }

        confirmVerified(productListView)
    }

    private fun `Then verify position is correct`(productItemViewModel: ProductItemViewModel) {
        val product = capturedProductItemViewModel.captured
        assert(product.position == productItemViewModel.position)
    }

    @Test
    fun `Handle onProductClick for non Top Ads Product`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Pixel 4"
            it.price = "Rp100.000.000"
            it.categoryID = 13
            it.isTopAds = false
        }
        val userId = "12345678"

        `Given user session data`(userId)

        `When handle product click`(productItemViewModel, position)

        `Then verify view interaction is correct for non Top Ads Product`(productItemViewModel, position, userId)
    }

    private fun `Given user session data`(userId: String) {
        every { userSession.isLoggedIn } returns true
        every { userSession.userId } returns userId
    }

    private fun `Then verify view interaction is correct for non Top Ads Product`(productItemViewModel: ProductItemViewModel, position: Int, userId: String) {
        verify {
            productListView.sendGTMTrackingProductClick(productItemViewModel, position, userId)
            productListView.routeToProductDetail(productItemViewModel, position)
        }

        confirmVerified(productListView)
    }
}
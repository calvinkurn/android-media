package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

internal class SearchProductHandleClickProductTest: ProductListPresenterTestFixtures() {

    private val adapterPosition = 1
    private val userId = "12345678"
    private val className = "SearchClassName"
    private val capturedProductItemViewModel = slot<ProductItemViewModel>()

    @Test
    fun `Handle onProductClick with null ProductItemViewModel`() {
        `When handle product click`(null)

        `Then verify view not doing anything`()
    }

    private fun `When handle product click`(productItemViewModel: ProductItemViewModel?) {
        productListPresenter.onProductClick(productItemViewModel, adapterPosition)
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
            it.imageUrl = imageUrl
            it.categoryID = 13
            it.isTopAds = true
            it.topadsImpressionUrl = topAdsImpressionUrl
            it.topadsClickUrl = topAdsClickUrl
            it.position = 1
        }

        `Given className from view`()

        `When handle product click`(productItemViewModel)

        `Then verify view interaction for Top Ads Product`(productItemViewModel)
        `Then verify position is correct`(productItemViewModel)
    }

    private fun `Given className from view`() {
        every { productListView.className } returns className
    }

    private fun `Then verify view interaction for Top Ads Product`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.className

            topAdsUrlHitter.hitClickUrl(
                    className,
                    productItemViewModel.topadsClickUrl,
                    productItemViewModel.productID,
                    productItemViewModel.productName,
                    productItemViewModel.imageUrl,
                    SearchConstant.TopAdsComponent.TOP_ADS
            )

            productListView.sendTopAdsGTMTrackingProductClick(capture(capturedProductItemViewModel))
            productListView.routeToProductDetail(productItemViewModel, adapterPosition)
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

        `Given user session data`()

        `When handle product click`(productItemViewModel)

        `Then verify view interaction is correct for non Top Ads Product`(productItemViewModel)
    }

    private fun `Given user session data`() {
        every { userSession.isLoggedIn } returns true
        every { userSession.userId } returns userId
    }

    private fun `Then verify view interaction is correct for non Top Ads Product`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.sendGTMTrackingProductClick(productItemViewModel, userId)
            productListView.routeToProductDetail(productItemViewModel, adapterPosition)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductClick for organic ads product`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Pixel 4"
            it.imageUrl = imageUrl
            it.price = "Rp100.000.000"
            it.categoryID = 13
            it.isTopAds = false
            it.isOrganicAds = true
            it.topadsImpressionUrl = topAdsImpressionUrl
            it.topadsClickUrl = topAdsClickUrl
        }

        `Given user session data`()
        `Given className from view`()

        `When handle product click`(productItemViewModel)

        `Then verify view interaction is correct for organic Ads Product`(productItemViewModel)
    }

    private fun `Then verify view interaction is correct for organic Ads Product`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.className

            topAdsUrlHitter.hitClickUrl(
                    className,
                    productItemViewModel.topadsClickUrl,
                    productItemViewModel.productID,
                    productItemViewModel.productName,
                    productItemViewModel.imageUrl,
                    SearchConstant.TopAdsComponent.ORGANIC_ADS
            )

            productListView.sendGTMTrackingProductClick(productItemViewModel, userId)
            productListView.routeToProductDetail(productItemViewModel, adapterPosition)
        }

        confirmVerified(productListView)
    }
}
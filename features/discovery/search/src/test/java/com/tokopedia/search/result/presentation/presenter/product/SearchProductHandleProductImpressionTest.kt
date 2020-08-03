package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

internal class SearchProductHandleProductImpressionTest: ProductListPresenterTestFixtures() {

    private val className = "SearchClassName"
    private val capturedProductItemViewModel = slot<ProductItemViewModel>()

    @Test
    fun `Handle onProductImpressed with null ProductItemViewModel (degenerate cases)`() {
        `When handle product impressed`(null)

        `Then verify view not doing anything`()
    }

    private fun `When handle product impressed`(productItemViewModel: ProductItemViewModel?) {
        productListPresenter.onProductImpressed(productItemViewModel)
    }

    private fun `Then verify view not doing anything`() {
        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductImpressed for Top Ads Product`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.imageUrl = imageUrl
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = true
            it.topadsImpressionUrl = topAdsImpressionUrl
            it.topadsClickUrl = topAdsClickUrl
            it.position = 1
        }

        `Given className from view`()

        `When handle product impressed`(productItemViewModel)

        `Then verify interaction for Top Ads product impression`(productItemViewModel)
    }

    private fun `Given className from view`() {
        every { productListView.className } returns className
    }

    private fun `Then verify interaction for Top Ads product impression`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    productItemViewModel.topadsImpressionUrl,
                    productItemViewModel.productID,
                    productItemViewModel.productName,
                    productItemViewModel.imageUrl
            )

            productListView.sendTopAdsGTMTrackingProductImpression(capture(capturedProductItemViewModel))
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductImpressed for organic Product`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = false
        }

        `Given className from view`()

        `When handle product impressed`(productItemViewModel)

        `Then verify interaction for product impression`(productItemViewModel)
    }

    private fun `Then verify interaction for product impression`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.sendProductImpressionTrackingEvent(productItemViewModel)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductImpressed for organic ads product`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.imageUrl = imageUrl
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = false
            it.isOrganicAds = true
            it.topadsClickUrl = topAdsClickUrl
            it.topadsImpressionUrl = topAdsImpressionUrl
        }

        `Given className from view`()

        `When handle product impressed`(productItemViewModel)

        `Then verify interaction for Organic Ads product impression`(productItemViewModel)
    }

    private fun `Then verify interaction for Organic Ads product impression`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.className

            topAdsUrlHitter.hitImpressionUrl(
                    className,
                    productItemViewModel.topadsImpressionUrl,
                    productItemViewModel.productID,
                    productItemViewModel.productName,
                    productItemViewModel.imageUrl
            )

            productListView.sendProductImpressionTrackingEvent(capture(capturedProductItemViewModel))
        }

        confirmVerified(productListView)
    }
}
package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.presenter.product.testinstance.topAdsClickUrl
import com.tokopedia.search.result.presentation.presenter.product.testinstance.topAdsImpressionUrl
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

internal class SearchProductHandleProductImpressionTest: ProductListPresenterTestFixtures() {

    private val position = 1
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
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = true
            it.topadsImpressionUrl = topAdsImpressionUrl
            it.topadsClickUrl = topAdsClickUrl
            it.position = 1
        }

        `When handle product impressed`(productItemViewModel)

        `Then verify interaction for Top Ads product impression`(productItemViewModel)
        `Then verify position is correct`(productItemViewModel)
    }

    private fun `Then verify interaction for Top Ads product impression`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.sendTopAdsTrackingUrl(productItemViewModel.topadsImpressionUrl)
            productListView.sendTopAdsGTMTrackingProductImpression(capture(capturedProductItemViewModel))
        }

        confirmVerified(productListView)
    }

    private fun `Then verify position is correct`(productItemViewModel: ProductItemViewModel) {
        val product = capturedProductItemViewModel.captured
        assert(product.position == productItemViewModel.position)
    }

    @Test
    fun `Handle onProductImpressed for non Top Ads Product when Tracking View Port Enabled`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = false
        }

        `When handle product impressed`(productItemViewModel)

        `Then verify enableTrackingViewPort is True`()
        `Then verify interaction for product impression`(productItemViewModel)
    }

    private fun `Then verify enableTrackingViewPort is True`() {
        assert(productListPresenter.isTrackingViewPortEnabled)
    }

    private fun `Then verify interaction for product impression`(productItemViewModel: ProductItemViewModel) {
        verify {
            productListView.sendProductImpressionTrackingEvent(productItemViewModel)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle onProductImpressed for non Top Ads Product when Tracking View Port Disabled`() {
        val productItemViewModel = ProductItemViewModel().also {
            it.productID = "12345"
            it.productName = "Hp Samsung"
            it.price = "Rp100.000"
            it.categoryID = 13
            it.isTopAds = false
        }

        `Given tracking by view port is disabled`()
        setUp()

        `When handle product impressed for disabled tracking view port`(productItemViewModel)

        `Then verify enableTrackingViewPort is False`()
        `Then verify interaction for product impression is not called`(productItemViewModel)
    }

    private fun `Given tracking by view port is disabled`() {
        every { remoteConfig.getBoolean(RemoteConfigKey.ENABLE_TRACKING_VIEW_PORT) } returns false
    }

    private fun `When handle product impressed for disabled tracking view port`(productItemViewModel: ProductItemViewModel?) {
        productListPresenter.onProductImpressed(productItemViewModel)
    }

    private fun `Then verify enableTrackingViewPort is False`() {
        assert(!productListPresenter.isTrackingViewPortEnabled)
    }

    private fun `Then verify interaction for product impression is not called`(productItemViewModel: ProductItemViewModel) {
        confirmVerified(productListView)
    }
}
package com.tokopedia.cart.view.viewmodel

import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import org.junit.Assert
import org.junit.Test

class CartShopGroupTickerAnalyticsTest : BaseCartViewModelTest() {

    @Test
    fun `WHEN generate cart bundling promotions analytics data with valid data THEN should generate valid promotions`() {
        // Given
        val bundleDetail = BundleDetailUiModel(
            products = listOf(
                BundleProductUiModel(
                    productId = "id_1",
                    productName = "name_1"
                ),
                BundleProductUiModel(
                    productId = "id_2",
                    productName = "name_2"
                )
            )
        )

        // When
        val result = cartViewModel.generateCartBundlingPromotionsAnalyticsData(bundleDetail)

        // Then
        Assert.assertTrue(result.size == 2)
    }

    @Test
    fun `WHEN generate cart bundling promotions analytics data with empty data THEN should generate empty promotions`() {
        // Given
        val bundleDetail = BundleDetailUiModel(
            products = emptyList()
        )

        // When
        val result = cartViewModel.generateCartBundlingPromotionsAnalyticsData(bundleDetail)

        // Then
        Assert.assertTrue(result.isEmpty())
    }
}

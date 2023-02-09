package com.tokopedia.tokopedianow.repurchase.presentation.listener

import com.tokopedia.productcard_compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics

class ProductCardCompactSimilarProductTrackerCallback(
    private val analytics: RepurchaseAnalytics
): ProductCardCompactSimilarProductTrackerListener {
    override fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        analytics.trackImpressionBottomSheet(
            userId = userId,
            warehouseId = warehouseId,
            similarProduct = similarProduct,
            productIdTriggered = productIdTriggered
        )
    }

    override fun trackClickProduct(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        analytics.trackClickProduct(
            userId = userId,
            warehouseId = warehouseId,
            similarProduct = similarProduct,
            productIdTriggered = productIdTriggered
        )
    }

    override fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String,
        newQuantity: Int
    ) {
        analytics.trackClickAddToCart(
            userId = userId,
            warehouseId = warehouseId,
            similarProduct = similarProduct,
            productIdTriggered = productIdTriggered,
            newQuantity = newQuantity
        )
    }

    override fun trackClickCloseBottomsheet(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        analytics.trackClickCloseBottomsheet(
            userId = userId,
            warehouseId = warehouseId,
            productIdTriggered = productIdTriggered
        )
    }

    override fun trackClickSimilarProductBtn(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        analytics.trackClickSimilarProductBtn(
            userId = userId,
            warehouseId = warehouseId,
            productIdTriggered = productIdTriggered
        )
    }

    override fun trackImpressionEmptyState(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        analytics.trackImpressionEmptyState(
            userId = userId,
            warehouseId = warehouseId,
            productIdTriggered = productIdTriggered
        )
    }
}

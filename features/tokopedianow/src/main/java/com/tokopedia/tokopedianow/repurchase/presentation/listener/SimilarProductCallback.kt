package com.tokopedia.tokopedianow.repurchase.presentation.listener

import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics
import com.tokopedia.tokopedianow.similarproduct.listener.SimilarProductListener
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

class SimilarProductCallback(
    private val analytics: RepurchaseAnalytics
): SimilarProductListener {
    override fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: SimilarProductUiModel,
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
        similarProduct: SimilarProductUiModel,
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
        similarProduct: SimilarProductUiModel,
        productIdTriggered: String
    ) {
        analytics.trackClickAddToCart(
            userId = userId,
            warehouseId = warehouseId,
            similarProduct = similarProduct,
            productIdTriggered = productIdTriggered
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

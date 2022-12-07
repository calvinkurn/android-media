package com.tokopedia.tokopedianow.repurchase.presentation.listener

import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics
import com.tokopedia.tokopedianow.similarproduct.listener.SimilarProductListener
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

class SimilarProductCallback(
    private val analytics: RepurchaseAnalytics,
    private val warehouseId: String,
    private val userId: String
): SimilarProductListener {
    override fun trackClickSimilarProductBtn(productId: String) {
        analytics.trackClickSimilarProductBtn(warehouseId, productId, userId)
    }

    override fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        productId: String,
        similarProducts: ArrayList<SimilarProductUiModel>
    ) {
        analytics.trackImpressionBottomSheet(warehouseId, productId, similarProducts, userId)
    }

    override fun trackClickProduct(
        userId: String,
        warehouseId: String,
        productId: String,
        similarProducts: ArrayList<SimilarProductUiModel>
    ) {
        analytics.trackClickProduct(warehouseId, productId, similarProducts, userId)
    }

    override fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        product: SimilarProductUiModel,
        similarProducts: ArrayList<SimilarProductUiModel>
    ) {
        analytics.trackClickAddToCart(userId, warehouseId, product, similarProducts)
    }

    override fun trackClickCloseBottomsheet(
        warehouseId: String,
        productId: String,
        similarProducts: ArrayList<SimilarProductUiModel>
    ) {
        analytics.trackClickCloseBottomsheet(warehouseId, productId, similarProducts, userId)
    }

    override fun trackImpressionEmptyState(warehouseId: String, productId: String) {
        analytics.trackImpressionEmptyState(warehouseId, productId, userId)
    }
}

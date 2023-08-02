package com.tokopedia.tokopedianow.category.presentation.callback

import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.tokopedianow.category.analytic.CategoryOosProductAnalytic

class ProductCardCompactSimilarProductTrackerCallback(
    private val categoryOosProductAnalytic: CategoryOosProductAnalytic
): ProductCardCompactSimilarProductTrackerListener {
    override fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        categoryOosProductAnalytic.trackImpressionBottomSheet(
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
        categoryOosProductAnalytic.trackClickProduct(
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
        categoryOosProductAnalytic.trackClickAddToCart(
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
        categoryOosProductAnalytic.trackClickCloseBottomsheet(
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
        categoryOosProductAnalytic.trackClickSimilarProductBtn(
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
        categoryOosProductAnalytic.trackImpressionEmptyState(
            userId = userId,
            warehouseId = warehouseId,
            productIdTriggered = productIdTriggered
        )
    }
}

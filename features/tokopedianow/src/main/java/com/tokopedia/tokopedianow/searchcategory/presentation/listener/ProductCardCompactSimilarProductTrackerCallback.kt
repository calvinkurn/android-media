package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.productcard_compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking
import com.tokopedia.tokopedianow.search.analytics.SearchTracking

class ProductCardCompactSimilarProductTrackerCallback(
    private val isCategoryPage: Boolean
): ProductCardCompactSimilarProductTrackerListener {
    override fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackImpressionBottomSheet(
                userId = userId,
                warehouseId = warehouseId,
                similarProduct = similarProduct,
                productIdTriggered = productIdTriggered
            )
        } else {
            SearchTracking.trackImpressionBottomSheet(
                userId = userId,
                warehouseId = warehouseId,
                similarProduct = similarProduct,
                productIdTriggered = productIdTriggered
            )
        }
    }

    override fun trackClickProduct(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackClickProduct(
                userId = userId,
                warehouseId = warehouseId,
                similarProduct = similarProduct,
                productIdTriggered = productIdTriggered
            )
        } else {
            SearchTracking.trackClickProduct(
                userId = userId,
                warehouseId = warehouseId,
                similarProduct = similarProduct,
                productIdTriggered = productIdTriggered
            )
        }
    }

    override fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String,
        newQuantity: Int
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackClickAddToCart(
                userId = userId,
                warehouseId = warehouseId,
                similarProduct = similarProduct,
                productIdTriggered = productIdTriggered,
                newQuantity = newQuantity
            )
        } else {
            SearchTracking.trackClickAddToCart(
                userId = userId,
                warehouseId = warehouseId,
                similarProduct = similarProduct,
                productIdTriggered = productIdTriggered,
                newQuantity = newQuantity
            )
        }
    }

    override fun trackClickCloseBottomsheet(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackClickCloseBottomsheet(
                userId = userId,
                warehouseId = warehouseId,
                productIdTriggered = productIdTriggered
            )
        } else {
            SearchTracking.trackClickCloseBottomsheet(
                userId = userId,
                warehouseId = warehouseId,
                productIdTriggered = productIdTriggered
            )
        }
    }

    override fun trackClickSimilarProductBtn(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackClickSimilarProductBtn(
                userId = userId,
                warehouseId = warehouseId,
                productIdTriggered = productIdTriggered
            )
        } else {
            SearchTracking.trackClickSimilarProductBtn(
                userId = userId,
                warehouseId = warehouseId,
                productIdTriggered = productIdTriggered
            )
        }
    }

    override fun trackImpressionEmptyState(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackImpressionEmptyState(
                userId = userId,
                warehouseId = warehouseId,
                productIdTriggered = productIdTriggered
            )
        } else {
            SearchTracking.trackImpressionEmptyState(
                userId = userId,
                warehouseId = warehouseId,
                productIdTriggered = productIdTriggered
            )
        }
    }
}

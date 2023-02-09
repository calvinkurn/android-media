package com.tokopedia.productcard_compact.similarproduct.presentation.listener

import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import java.io.Serializable

interface ProductCardCompactSimilarProductTrackerListener: Serializable {
    fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    )

    fun trackClickProduct(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    )

    fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String,
        newQuantity: Int
    )

    fun trackClickCloseBottomsheet(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    )

    fun trackClickSimilarProductBtn(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    )

    fun trackImpressionEmptyState(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    )
}

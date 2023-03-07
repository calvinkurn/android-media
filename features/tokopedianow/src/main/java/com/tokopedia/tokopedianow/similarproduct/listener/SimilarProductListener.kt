package com.tokopedia.tokopedianow.similarproduct.listener

import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import java.io.Serializable

interface SimilarProductListener: Serializable {
    fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: SimilarProductUiModel,
        productIdTriggered: String
    )

    fun trackClickProduct(
        userId: String,
        warehouseId: String,
        similarProduct: SimilarProductUiModel,
        productIdTriggered: String
    )

    fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        similarProduct: SimilarProductUiModel,
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

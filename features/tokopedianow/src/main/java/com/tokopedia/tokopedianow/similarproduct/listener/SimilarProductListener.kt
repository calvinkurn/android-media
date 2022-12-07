package com.tokopedia.tokopedianow.similarproduct.listener

import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import java.io.Serializable

interface SimilarProductListener: Serializable {
    fun trackClickSimilarProductBtn(productId: String)

    fun trackImpressionBottomSheet(userId: String, warehouseId: String, productId: String, similarProducts: ArrayList<SimilarProductUiModel>)

    fun trackClickProduct(userId: String, warehouseId: String, productId: String, similarProducts: ArrayList<SimilarProductUiModel>)

    fun trackClickAddToCart(userId: String, warehouseId: String, product: SimilarProductUiModel, similarProducts: ArrayList<SimilarProductUiModel>)

    fun trackClickCloseBottomsheet(warehouseId: String, productId: String, similarProducts: ArrayList<SimilarProductUiModel>)

    fun trackImpressionEmptyState(warehouseId: String, productId: String)
}

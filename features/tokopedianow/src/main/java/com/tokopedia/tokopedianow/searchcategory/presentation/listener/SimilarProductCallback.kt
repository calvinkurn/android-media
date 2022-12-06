package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.tokopedianow.category.analytics.CategoryTracking
import com.tokopedia.tokopedianow.search.analytics.SearchTracking
import com.tokopedia.tokopedianow.similarproduct.listener.SimilarProductListener
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

class SimilarProductCallback(
    private val warehouseId: String,
    private val userId: String,
    private val isCategoryPage: Boolean
): SimilarProductListener {
    override fun trackClickSimilarProductBtn(productId: String) {
        if (isCategoryPage) {
            CategoryTracking.trackClickSimilarProductBtn(warehouseId, productId, userId)
        } else {
            SearchTracking.trackClickSimilarProductBtn(warehouseId, productId, userId)
        }
    }

    override fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        productId: String,
        similarProducts: ArrayList<SimilarProductUiModel>
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackImpressionBottomSheet(warehouseId, productId, similarProducts, userId)
        } else {
            SearchTracking.trackImpressionBottomSheet(warehouseId, productId, similarProducts, userId)
        }
    }

    override fun trackClickProduct(
        userId: String,
        warehouseId: String,
        productId: String,
        similarProducts: ArrayList<SimilarProductUiModel>
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackClickProduct(warehouseId, productId, similarProducts, userId)
        } else {
            SearchTracking.trackClickProduct(warehouseId, productId, similarProducts, userId)
        }
    }

    override fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        product: SimilarProductUiModel,
        similarProducts: ArrayList<SimilarProductUiModel>
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackClickAddToCart(userId, warehouseId, product, similarProducts)
        } else {
            SearchTracking.trackClickAddToCart(userId, warehouseId, product, similarProducts)
        }
    }

    override fun trackClickCloseBottomsheet(
        warehouseId: String,
        productId: String,
        similarProducts: ArrayList<SimilarProductUiModel>
    ) {
        if (isCategoryPage) {
            CategoryTracking.trackClickCloseBottomsheet(warehouseId, productId, similarProducts, userId)
        } else {
            SearchTracking.trackClickCloseBottomsheet(warehouseId, productId, similarProducts, userId)
        }
    }

    override fun trackImpressionEmptyState(warehouseId: String, productId: String) {
        if (isCategoryPage) {
            CategoryTracking.trackImpressionEmptyState(warehouseId, productId, userId)
        } else {
            SearchTracking.trackImpressionEmptyState(warehouseId, productId, userId)
        }
    }
}

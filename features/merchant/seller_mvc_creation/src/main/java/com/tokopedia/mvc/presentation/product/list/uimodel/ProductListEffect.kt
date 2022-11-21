package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.ShopData

sealed class ProductListEffect {
    data class ShowVariantBottomSheet(
        val isParentProductSelected: Boolean,
        val selectedProduct: SelectedProduct,
        val originalVariantIds: List<Long>
    ) : ProductListEffect()

    data class ConfirmAddProduct(
        val selectedProducts: List<SelectedProduct>,
        val selectedParentProductImageUrls: List<String>
    ) : ProductListEffect()

    data class ShowDeleteProductConfirmationDialog(val productId: Long) : ProductListEffect()
    data class ShowBulkDeleteProductConfirmationDialog(val toDeleteProductCount: Int) : ProductListEffect()

    object ProductDeleted : ProductListEffect()
    data class BulkDeleteProductSuccess(val deletedProductCount : Int) : ProductListEffect()
    data class ShowError(val error: Throwable) : ProductListEffect()
}

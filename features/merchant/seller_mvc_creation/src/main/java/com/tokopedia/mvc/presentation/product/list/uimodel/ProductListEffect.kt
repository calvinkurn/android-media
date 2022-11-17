package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.ShopData
import com.tokopedia.mvc.presentation.product.variant.review.uimodel.ReviewVariantEffect

sealed class ProductListEffect {
    data class ShowVariantBottomSheet(
        val isParentProductSelected: Boolean,
        val selectedProduct: SelectedProduct,
        val originalVariantIds: List<Long>
    ) : ProductListEffect()

    data class ConfirmAddProduct(
        val selectedParentProducts: List<Product>,
        val selectedParentProductImageUrls: List<String>,
        val shop: ShopData
    ) : ProductListEffect()

    object ProductDeleted : ProductListEffect()
    data class BulkDeleteProductSuccess(val deletedProductCount : Int) : ProductListEffect()
}

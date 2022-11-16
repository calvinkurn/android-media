package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ShopData

sealed class ProductListEffect {
    data class ShowVariantBottomSheet(val selectedParentProduct: Product) : ProductListEffect()
    data class ConfirmAddProduct(
        val selectedParentProducts: List<Product>,
        val selectedParentProductImageUrls: List<String>,
        val shop: ShopData
    ) : ProductListEffect()
}

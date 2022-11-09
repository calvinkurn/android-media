package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductSortOptions

sealed class AddProductEffect {
    data class LoadNextPageSuccess(
        val currentPageItems: List<Product>,
        val allItems: List<Product>
    ) : AddProductEffect()

    data class ShowSortBottomSheet(
        val sortOptions: List<ProductSortOptions>,
        val selectedSort: ProductSortOptions
    ) : AddProductEffect()
}

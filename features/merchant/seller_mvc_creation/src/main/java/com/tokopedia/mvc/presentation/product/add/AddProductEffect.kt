package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.mvc.domain.entity.Product

sealed class AddProductEffect {
    data class LoadNextPageSuccess(
        val currentPageItems: List<Product>,
        val allItems: List<Product>
    ) : AddProductEffect()
}

package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.Product

data class ProductListUiState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val selectedProductsIds: Set<Long> = emptySet(),
    val isSelectAllActive: Boolean = false,
    val error: Throwable? = null
)

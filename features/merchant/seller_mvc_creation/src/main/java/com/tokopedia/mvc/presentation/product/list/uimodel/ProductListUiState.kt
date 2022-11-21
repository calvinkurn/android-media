package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.enums.PageMode

data class ProductListUiState(
    val isLoading: Boolean = true,
    val pageMode: PageMode = PageMode.CREATE,
    val products: List<Product> = emptyList(),
    val maxProductSelection : Int = 0,
    val selectedProductsIds: Set<Long> = emptySet(),
    val isSelectAllActive: Boolean = false,
    val error: Throwable? = null
)

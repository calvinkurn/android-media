package com.tokopedia.mvc.presentation.product.variant.uimodel

import com.tokopedia.mvc.domain.entity.Product

data class SelectVariantUiState(
    val isLoading: Boolean = true,
    val parentProduct: Product = Product(
        0,
        true,
        "",
        "",
        Product.Preorder(0),
        Product.Price(0, 0),
        "",
        Product.Stats(0, 0, 0),
        "", 0, Product.TxStats(0), emptyList(), 0, true, "",
        emptyList(), emptyList(), false, false
    ),
    val isSelectAllActive: Boolean = true,
    val error: Throwable? = null
)

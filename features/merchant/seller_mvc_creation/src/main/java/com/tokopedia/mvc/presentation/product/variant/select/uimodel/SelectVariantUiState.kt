package com.tokopedia.mvc.presentation.product.variant.select.uimodel

import com.tokopedia.mvc.domain.entity.Variant

data class SelectVariantUiState(
    val isLoading: Boolean = true,
    val parentProductId: Long = 0,
    val parentProductStock: Int = 0,
    val parentProductName: String = "",
    val parentProductPrice: Long = 0,
    val parentProductSoldCount: Int = 0,
    val parentProductImageUrl: String = "",
    val variants: List<Variant> = emptyList(),
    val selectedVariantIds: Set<Long> = emptySet(),
    val isSelectAllActive: Boolean = true,
    val error: Throwable? = null
)

package com.tokopedia.mvc.presentation.product.variant.review.uimodel

import com.tokopedia.mvc.domain.entity.Variant

data class ReviewVariantUiState(
    val isLoading: Boolean = true,
    val isParentProductSelected: Boolean = false,
    val parentProductId: Long = 0,
    val parentProductStock: Int = 0,
    val parentProductName: String = "",
    val parentProductPrice: Long = 0,
    val parentProductSoldCount: Int = 0,
    val parentProductImageUrl: String = "",
    val originalVariantIds: List<Long> = emptyList(),
    val selectedVariantIds: Set<Long> = emptySet(),
    val variants: List<Variant> = emptyList(),
    val isSelectAllActive: Boolean = true,
    val error: Throwable? = null
)

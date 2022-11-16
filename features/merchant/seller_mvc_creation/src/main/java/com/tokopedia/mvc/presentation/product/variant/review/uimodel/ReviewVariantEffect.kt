package com.tokopedia.mvc.presentation.product.variant.review.uimodel

sealed class ReviewVariantEffect {
    data class ConfirmUpdateVariant(val selectedVariantIds: Set<Long>) : ReviewVariantEffect()
}

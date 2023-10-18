package com.tokopedia.mvc.presentation.product.variant.review.uimodel

sealed class ReviewVariantEffect {
    data class ConfirmUpdateVariant(val selectedVariantIds: Set<Long>) : ReviewVariantEffect()
    data class ShowDeleteVariantConfirmationDialog(val productId: Long) : ReviewVariantEffect()
    data class ShowBulkDeleteVariantConfirmationDialog(val toDeleteProductCount: Int) : ReviewVariantEffect()
    data class ShowError(val error: Throwable) : ReviewVariantEffect()
}

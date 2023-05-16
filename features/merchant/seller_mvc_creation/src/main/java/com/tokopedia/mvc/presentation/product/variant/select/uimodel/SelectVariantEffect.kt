package com.tokopedia.mvc.presentation.product.variant.select.uimodel

sealed class SelectVariantEffect {
    data class ConfirmUpdateVariant(val selectedVariantIds: Set<Long>) : SelectVariantEffect()
    data class ShowError(val error: Throwable) : SelectVariantEffect()
}

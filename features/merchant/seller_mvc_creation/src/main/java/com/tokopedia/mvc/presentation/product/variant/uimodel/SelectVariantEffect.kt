package com.tokopedia.mvc.presentation.product.variant.uimodel

sealed class SelectVariantEffect {
    data class ConfirmUpdateVariant(val selectedVariantIds: Set<Long>) : SelectVariantEffect()
}

package com.tokopedia.mvc.presentation.product.variant.uimodel

import com.tokopedia.mvc.domain.entity.Product

sealed class SelectVariantEffect {
    data class ConfirmUpdateVariant(val modifiedParentProduct: Product) : SelectVariantEffect()
}

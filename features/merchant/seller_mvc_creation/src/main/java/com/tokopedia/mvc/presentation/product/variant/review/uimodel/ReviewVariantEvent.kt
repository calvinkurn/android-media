package com.tokopedia.mvc.presentation.product.variant.review.uimodel

import com.tokopedia.mvc.domain.entity.SelectedProduct

sealed class ReviewVariantEvent {
    data class FetchProductVariants(
        val isParentProductSelected: Boolean,
        val selectedProduct: SelectedProduct
    ) : ReviewVariantEvent()

    object EnableSelectAllCheckbox : ReviewVariantEvent()
    object DisableSelectAllCheckbox : ReviewVariantEvent()
    data class AddVariantToSelection(val variantProductId: Long) : ReviewVariantEvent()
    data class RemoveVariantFromSelection(val variantProductId: Long) : ReviewVariantEvent()
    data class RemoveVariant(val productId: Long) : ReviewVariantEvent()
    object TapSelectButton : ReviewVariantEvent()
}

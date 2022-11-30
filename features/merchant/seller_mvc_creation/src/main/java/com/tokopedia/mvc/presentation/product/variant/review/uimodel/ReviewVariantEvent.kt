package com.tokopedia.mvc.presentation.product.variant.review.uimodel

import com.tokopedia.mvc.domain.entity.SelectedProduct

sealed class ReviewVariantEvent {
    data class FetchProductVariants(
        val isParentProductSelected: Boolean,
        val selectedProduct: SelectedProduct,
        val originalVariantIds: List<Long>,
        val isVariantCheckable: Boolean,
        val isVariantDeletable: Boolean
    ) : ReviewVariantEvent()

    object EnableSelectAllCheckbox : ReviewVariantEvent()
    object DisableSelectAllCheckbox : ReviewVariantEvent()
    data class AddVariantToSelection(val variantProductId: Long) : ReviewVariantEvent()
    data class RemoveVariantFromSelection(val variantProductId: Long) : ReviewVariantEvent()
    object TapSelectButton : ReviewVariantEvent()

    data class TapRemoveVariant(val variantId: Long) : ReviewVariantEvent()
    data class ApplyRemoveVariant(val variantId: Long) : ReviewVariantEvent()
    object TapBulkDeleteVariant : ReviewVariantEvent()
    object ApplyBulkDeleteVariant : ReviewVariantEvent()
}

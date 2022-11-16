package com.tokopedia.mvc.presentation.product.variant.review.uimodel

import com.tokopedia.mvc.domain.entity.SelectedProduct

sealed class ReviewVariantEvent {
    data class FetchProductVariants(val selectedProduct: SelectedProduct) : ReviewVariantEvent()
    object EnableSelectAllCheckbox : ReviewVariantEvent()
    object DisableSelectAllCheckbox : ReviewVariantEvent()
    data class AddProductToSelection(val variantProductId: Long) : ReviewVariantEvent()
    data class RemoveProductFromSelection(val variantProductId: Long) : ReviewVariantEvent()
    data class RemoveProduct(val productId: Long) : ReviewVariantEvent()
    object TapSelectButton : ReviewVariantEvent()
}

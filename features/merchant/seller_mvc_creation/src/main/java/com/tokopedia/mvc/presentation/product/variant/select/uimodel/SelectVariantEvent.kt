package com.tokopedia.mvc.presentation.product.variant.select.uimodel

import com.tokopedia.mvc.domain.entity.Product

sealed class SelectVariantEvent {
    data class FetchProductVariants(val selectedParentProduct: Product) : SelectVariantEvent()
    object EnableSelectAllCheckbox : SelectVariantEvent()
    object DisableSelectAllCheckbox : SelectVariantEvent()
    data class AddProductToSelection(val variantProductId: Long) : SelectVariantEvent()
    data class RemoveProductFromSelection(val variantProductId: Long) : SelectVariantEvent()
    object TapSelectButton : SelectVariantEvent()
}

package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction

sealed class ProductListEvent {
    data class FetchProducts(
        val action: VoucherAction,
        val promoType: PromoType,
        val selectedProducts: List<SelectedProduct>
    ) : ProductListEvent()

    object EnableSelectAllCheckbox : ProductListEvent()
    object DisableSelectAllCheckbox : ProductListEvent()
    data class AddProductToSelection(val productId: Long) : ProductListEvent()
    data class RemoveProductFromSelection(val productId: Long) : ProductListEvent()
    data class TapRemoveProduct(val productId: Long) : ProductListEvent()
    data class ApplyRemoveProduct(val productId: Long) : ProductListEvent()
    object TapBulkDeleteProduct : ProductListEvent()
    object ApplyBulkDeleteProduct : ProductListEvent()
    object ConfirmAddProduct : ProductListEvent()
    data class TapVariant(val parentProduct: Product) : ProductListEvent()
    data class VariantUpdated(
        val modifiedParentProductId: Long,
        val selectedVariantIds: Set<Long>
    ) : ProductListEvent()
}

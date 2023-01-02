package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode

sealed class ProductListEvent {
    data class FetchProducts(
        val pageMode: PageMode,
        val voucherConfiguration: VoucherConfiguration,
        val selectedProducts: List<SelectedProduct>,
        val showCtaUpdateProductOnToolbar: Boolean,
        val isEntryPointFromVoucherSummaryPage: Boolean,
        val selectedWarehouseId: Long
    ) : ProductListEvent()
    object EnableSelectAllCheckbox : ProductListEvent()
    object DisableSelectAllCheckbox : ProductListEvent()
    data class MarkProductForDeletion(val productId: Long) : ProductListEvent()
    data class AddNewProductToSelection(val newProducts: List<Product>) : ProductListEvent()
    data class RemoveProductFromSelection(val productId: Long) : ProductListEvent()
    data class TapRemoveProduct(val productId: Long) : ProductListEvent()
    data class ApplyRemoveProduct(val productId: Long) : ProductListEvent()
    object TapBulkDeleteProduct : ProductListEvent()
    object ApplyBulkDeleteProduct : ProductListEvent()
    object TapContinueButton : ProductListEvent()
    data class TapVariant(val parentProduct: Product) : ProductListEvent()
    data class VariantUpdated(
        val modifiedParentProductId: Long,
        val selectedVariantIds: Set<Long>
    ) : ProductListEvent()
    object TapCtaChangeProduct : ProductListEvent()
    object TapCtaAddProduct: ProductListEvent()
}

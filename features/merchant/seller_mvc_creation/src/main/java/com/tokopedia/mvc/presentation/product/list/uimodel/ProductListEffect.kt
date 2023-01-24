package com.tokopedia.mvc.presentation.product.list.uimodel

import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode

sealed class ProductListEffect {
    data class ShowVariantBottomSheet(
        val isParentProductSelected: Boolean,
        val selectedProduct: SelectedProduct,
        val originalVariantIds: List<Long>,
        val pageMode: PageMode
    ) : ProductListEffect()

    data class ProceedToVoucherPreviewPage(
        val voucherConfiguration: VoucherConfiguration,
        val selectedProducts: List<SelectedProduct>,
        val selectedParentProductImageUrls: List<String>,
        val pageMode: PageMode
    ) : ProductListEffect()

    data class RedirectToAddProductPage(val voucherConfiguration: VoucherConfiguration) : ProductListEffect()
    object BackToPreviousPage : ProductListEffect()

    data class ShowDeleteProductConfirmationDialog(val productId: Long) : ProductListEffect()
    data class ShowBulkDeleteProductConfirmationDialog(val toDeleteProductCount: Int) : ProductListEffect()

    object ProductDeleted : ProductListEffect()
    data class BulkDeleteProductSuccess(val deletedProductCount : Int) : ProductListEffect()
    data class ShowError(val error: Throwable) : ProductListEffect()
    data class RedirectToPreviousPage(val selectedProductCount: Int, val pageMode: PageMode) : ProductListEffect()
}

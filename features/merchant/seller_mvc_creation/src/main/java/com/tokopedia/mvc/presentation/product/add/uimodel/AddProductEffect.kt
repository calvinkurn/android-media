package com.tokopedia.mvc.presentation.product.add.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.Warehouse

sealed class AddProductEffect {
    data class LoadNextPageSuccess(
        val allItemCount: Int,
        val totalProductCount: Int
    ) : AddProductEffect()

    data class ShowSortBottomSheet(
        val sortOptions: List<ProductSortOptions>,
        val selectedSort: ProductSortOptions
    ) : AddProductEffect()

    data class ShowProductCategoryBottomSheet(
        val categories: List<ProductCategoryOption>,
        val selectedCategories: List<ProductCategoryOption>
    ) : AddProductEffect()

    data class ShowShowcasesBottomSheet(
        val showcases: List<ShopShowcase>,
        val selectedShowcaseIds: List<Long>
    ) : AddProductEffect()

    data class ShowWarehouseLocationBottomSheet(
        val warehouses: List<Warehouse>,
        val selectedWarehouse: Warehouse
    ) : AddProductEffect()

    data class ShowVariantBottomSheet(val selectedParentProduct: Product) : AddProductEffect()

    data class ProductConfirmed(
        val selectedProducts: List<SelectedProduct>,
        val selectedParentProductImageUrls: List<String>,
        val voucherConfiguration: VoucherConfiguration,
        val selectedWarehouseId: Long
    ) : AddProductEffect()

    data class AddNewProducts(
        val selectedProducts: List<Product>
    ) : AddProductEffect()

    data class ShowChangeWarehouseDialogConfirmation(val selectedWarehouseLocation: Warehouse) : AddProductEffect()

    data class ShowError(val error: Throwable) : AddProductEffect()
}

package com.tokopedia.mvc.presentation.product.add.uimodel

import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.PageMode

sealed class AddProductEvent {
    data class FetchRequiredData(
        val pageMode: PageMode,
        val voucherConfiguration: VoucherConfiguration,
        val products: List<Product>
    ) : AddProductEvent()
    data class LoadPage(val page: Int) : AddProductEvent()
    object ClearSearchBar : AddProductEvent()
    object ClearFilter : AddProductEvent()
    object EnableSelectAllCheckbox : AddProductEvent()
    object DisableSelectAllCheckbox : AddProductEvent()
    data class AddProductToSelection(val productId: Long) : AddProductEvent()
    data class RemoveProductFromSelection(val productId: Long) : AddProductEvent()
    object TapWarehouseLocationFilter : AddProductEvent()
    object TapCategoryFilter : AddProductEvent()
    object TapShowCaseFilter : AddProductEvent()
    object TapSortFilter : AddProductEvent()
    data class ApplyWarehouseLocationFilter(val selectedWarehouseLocation: Warehouse) : AddProductEvent()
    data class ApplyCategoryFilter(val selectedCategories: List<ProductCategoryOption>) : AddProductEvent()
    data class ApplyShowCaseFilter(val selectedShowCases: List<ShopShowcase>) : AddProductEvent()
    data class ApplySortFilter(val selectedSort: ProductSortOptions) : AddProductEvent()
    data class SearchProduct(val searchKeyword : String) : AddProductEvent()
    object ConfirmAddProduct : AddProductEvent()
    object AddNewProducts : AddProductEvent()
    data class ConfirmChangeWarehouseLocationFilter(val newWarehouseLocation: Warehouse) : AddProductEvent()
    data class TapVariant(val parentProduct : Product) : AddProductEvent()
    data class VariantUpdated(val modifiedParentProductId: Long, val selectedVariantIds: Set<Long>) : AddProductEvent()
}

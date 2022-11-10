package com.tokopedia.mvc.presentation.product.add.uimodel

import com.tokopedia.mvc.domain.entity.ProductCategoryOption
import com.tokopedia.mvc.domain.entity.ProductSortOptions
import com.tokopedia.mvc.domain.entity.ShopShowcase
import com.tokopedia.mvc.domain.entity.Warehouse
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction

sealed class AddProductEvent {
    data class FetchRequiredData(val action: VoucherAction, val promoType: PromoType) : AddProductEvent()

    data class LoadPage(val page: Int) : AddProductEvent()

    object ClearSearchBar : AddProductEvent()
    object ClearFilter : AddProductEvent()
    object EnableSelectAllCheckbox : AddProductEvent()
    object DisableSelectAllCheckbox : AddProductEvent()
    data class AddProductToSelection(val productId: Long) : AddProductEvent()
    data class RemoveProductFromSelection(val productId: Long) : AddProductEvent()
    object TapLocationFilter : AddProductEvent()
    object TapCategoryFilter : AddProductEvent()
    object TapShowCaseFilter : AddProductEvent()
    object TapSortFilter : AddProductEvent()
    data class ApplyWarehouseLocationFilter(val selectedWarehouseLocation: Warehouse) : AddProductEvent()
    data class ApplyCategoryFilter(val selectedCategories: List<ProductCategoryOption>) : AddProductEvent()
    data class ApplyShowCaseFilter(val selectedShowCase: ShopShowcase) : AddProductEvent()
    data class ApplySortFilter(val selectedSort: ProductSortOptions) : AddProductEvent()
    object ConfirmAddProduct : AddProductEvent()
}

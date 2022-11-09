package com.tokopedia.mvc.presentation.product.add

import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherAction

sealed class AddProductEvent {
    data class FetchRequiredData(val action: VoucherAction, val promoType: PromoType) : AddProductEvent()

    data class LoadPage(
        val warehouseId: Long,
        val page: Int,
        val sortId: String,
        val sortDirection: String
    ) : AddProductEvent()

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
    object ApplyLocationFilter : AddProductEvent()
    object ApplyCategoryFilter : AddProductEvent()
    object ApplyShowCaseFilter : AddProductEvent()
    object ApplySortFilter : AddProductEvent()
    object ConfirmAddProduct : AddProductEvent()
}

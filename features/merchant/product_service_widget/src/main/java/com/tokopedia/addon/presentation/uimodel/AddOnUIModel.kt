package com.tokopedia.addon.presentation.uimodel

import com.tokopedia.addon.presentation.uimodel.AddOnType.PRODUCT_PROTECTION_INSURANCE_TYPE
import java.io.Serializable

data class AddOnUIModel(
    var id: String = "",
    var name: String = "",
    var priceFormatted: String = "",
    var price: Long = 0L,
    var isSelected: Boolean = false,
    var addOnType: AddOnType = PRODUCT_PROTECTION_INSURANCE_TYPE,
    var eduLink: String = "",
    var uniqueId: String = ""
): Serializable

data class AddOnGroupUIModel(
    var title: String = "",
    var iconUrl: String = "",
    var iconDarkmodeUrl: String = "",
    var productId: Long = 0L,
    var warehouseId: Long = 0L,
    var addOnLevel: String = "",
    var addon: List<AddOnUIModel> = emptyList()
)

class AddOnPageResult(
    val selectedAddons: List<AddOnUIModel> = emptyList(),
    val aggregatedData: AggregatedData = AggregatedData()
): Serializable {
    data class AggregatedData(
        val title: String = "",
        val price: Long = 0,
        val isGetDataSuccess: Boolean = false,
        val getDataErrorMessage: String = "",
    ): Serializable
}

package com.tokopedia.addon.presentation.uimodel

import com.tokopedia.addon.presentation.uimodel.AddOnType.PRODUCT_PROTECTION_INSURANCE_TYPE

data class AddOnUIModel(
    var id: String = "",
    var name: String = "",
    var priceFormatted: String = "",
    var isSelected: Boolean = false,
    var addOnType: AddOnType = PRODUCT_PROTECTION_INSURANCE_TYPE
)

data class AddOnGroupUIModel(
    var title: String = "",
    var iconUrl: String = "",
    var iconDarkmodeUrl: String = "",
    var addon: List<AddOnUIModel> = emptyList()
)

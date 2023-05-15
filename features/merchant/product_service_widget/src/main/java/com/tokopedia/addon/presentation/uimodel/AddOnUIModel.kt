package com.tokopedia.addon.presentation.uimodel

data class AddOnUIModel(
    var name: String = "",
    var priceFormatted: String = "",
    var isSelected: Boolean = false
)

data class AddOnGroupUIModel(
    var title: String = "",
    var iconUrl: String = "",
    var iconDarkmodeUrl: String = "",
    var addon: List<AddOnUIModel> = emptyList()
)

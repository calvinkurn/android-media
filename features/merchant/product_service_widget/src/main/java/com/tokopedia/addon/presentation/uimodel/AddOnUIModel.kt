package com.tokopedia.addon.presentation.uimodel

data class AddOnUIModel(
    val name: String = "",
    val priceFormatted: String = "",
    val isSelected: Boolean = false
)

data class AddOnGroupUIModel(
    val title: String = "",
    val iconUrl: String = "",
    val iconDarkmodeUrl: String = "",
    val addon: List<AddOnUIModel> = emptyList()
)

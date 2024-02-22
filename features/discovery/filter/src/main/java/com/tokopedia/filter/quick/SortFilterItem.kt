package com.tokopedia.filter.quick

data class SortFilterItem(
    val title: String = "",
    val isSelected: Boolean = false,
    val iconUrl: String = "",
    val hasChevron: Boolean = false,
    val shouldShowImage: Boolean = false,
    val imageUrlActive: String = "",
    val imageUrlInactive: String = "",
    val position: Int = 0
)

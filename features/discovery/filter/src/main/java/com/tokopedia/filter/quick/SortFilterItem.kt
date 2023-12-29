package com.tokopedia.filter.quick

data class SortFilterItem(
    val title: String = "",
    val isSelected: Boolean = false,
    val iconUrl: String = "",
    val hasChevron: Boolean = false,
)

package com.tokopedia.autocompletecomponent.searchbar

data class SearchBarKeyword(
    val position: Int = 0,
    val keyword: String = "",
    val shouldShowCoachMark: Boolean = false,
)

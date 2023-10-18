package com.tokopedia.autocompletecomponent.searchbar

data class SearchBarKeyword(
    val position: Int = 0,
    val keyword: String = "",
    val shouldShowCoachMark: Boolean = false,
    val isSelected: Boolean = false,
) {
    fun hasSameKeywordAndPosition(searchBarKeyword: SearchBarKeyword) :Boolean {
        return keyword == searchBarKeyword.keyword && position == searchBarKeyword.position
    }
}

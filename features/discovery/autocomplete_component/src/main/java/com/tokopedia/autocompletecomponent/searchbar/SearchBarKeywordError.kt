package com.tokopedia.autocompletecomponent.searchbar

sealed interface SearchBarKeywordError {
    object Empty : SearchBarKeywordError
    object Duplicate : SearchBarKeywordError
}

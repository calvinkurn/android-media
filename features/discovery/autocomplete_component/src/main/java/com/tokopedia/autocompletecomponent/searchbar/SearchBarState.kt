package com.tokopedia.autocompletecomponent.searchbar

data class SearchBarState(
    val isMpsAnimationEnabled: Boolean = false,
    val shouldShowCoachMark: Boolean = false,
    val isAddButtonEnabled: Boolean = false,
    val isKeyboardDismissEnabled: Boolean = true,
    val hasHintOrPlaceHolder: Boolean = false,
    val hintText: String = "",
    val shouldDisplayMpsPlaceHolder: Boolean = false,
)

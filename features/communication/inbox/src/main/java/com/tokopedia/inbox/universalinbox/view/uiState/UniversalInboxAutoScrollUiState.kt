package com.tokopedia.inbox.universalinbox.view.uiState

data class UniversalInboxAutoScrollUiState(
    val shouldScroll: Boolean = false,
    val totalItem: Int = 0,
    val currentPosition: Int = -1,
    val toPosition: Int = -1
)

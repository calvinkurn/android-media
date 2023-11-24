package com.tokopedia.inbox.universalinbox.view.uiState

data class UniversalInboxErrorUiState(
    val error: Pair<Throwable, String>? = null
)

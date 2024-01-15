package com.tokopedia.shareexperience.ui.uistate

data class ShareExShortLinkUiState(
    val shortLinkUrl: String = "",
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val showToaster: Boolean = false
)

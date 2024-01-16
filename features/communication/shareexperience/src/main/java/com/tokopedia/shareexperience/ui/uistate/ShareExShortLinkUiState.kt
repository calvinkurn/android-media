package com.tokopedia.shareexperience.ui.uistate

import android.content.Intent

data class ShareExShortLinkUiState(
    val intent: Intent? = null,
    val message: String = "",
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val showToaster: Boolean = false
)

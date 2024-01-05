package com.tokopedia.inbox.universalinbox.view.uiState

import android.content.Intent
import com.tokopedia.inbox.universalinbox.view.UniversalInboxRequestType

data class UniversalInboxNavigationUiState(
    val applink: String = "",
    val intent: Intent? = null,
    val requestType: UniversalInboxRequestType
)

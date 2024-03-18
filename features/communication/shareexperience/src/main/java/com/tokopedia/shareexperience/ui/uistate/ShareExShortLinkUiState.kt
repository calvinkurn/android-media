package com.tokopedia.shareexperience.ui.uistate

import android.net.Uri

data class ShareExShortLinkUiState(
    val shortLink: String = "",
    val imageUri: Uri? = null
)

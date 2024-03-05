package com.tokopedia.shareexperience.ui.uistate

import android.content.Intent
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum
import com.tokopedia.shareexperience.ui.util.ShareExIntentErrorEnum

data class ShareExChannelIntentUiState(
    val intent: Intent? = null,
    val message: String = "",
    val shortLink: String = "",
    val channelEnum: ShareExChannelEnum? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val imageType: ShareExImageTypeEnum = ShareExImageTypeEnum.NO_IMAGE,
    val errorHistory: List<ShareExIntentErrorEnum> = listOf()
)

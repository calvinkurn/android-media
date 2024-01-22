package com.tokopedia.shareexperience.ui.uistate

import android.content.Intent
import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExImageTypeEnum

data class ShareExChannelIntentUiState(
    val intent: Intent? = null,
    val message: String = "",
    val shortLink: String = "",
    val channelEnum: ShareExChannelEnum? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val imageType: ShareExImageTypeEnum = ShareExImageTypeEnum.NO_IMAGE,

    /**
     * Special case for affiliate
     * Do not change the value when updating except when affiliate is error
     *
     */
    val isAffiliateError: Boolean = false
)

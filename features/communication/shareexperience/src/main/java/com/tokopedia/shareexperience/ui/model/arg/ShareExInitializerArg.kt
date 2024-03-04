package com.tokopedia.shareexperience.ui.model.arg

import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.ui.uistate.ShareExInitializationUiState

/**
 * Optional Initializer args
 * Currently it's used to determine whether the share is eligible to affiliate or not
 * For example, in PDP, when share is eligible to affiliate, the icon is changed to new share icon
 * Do not use this initializer when you don't need it
 * Directly use [ShareExBottomSheetArg] instead
 */
data class ShareExInitializerArg(
    val affiliateEligibilityRequest: ShareExAffiliateEligibilityRequest? = null,
    val onSuccess: (ShareExInitializationUiState) -> Unit = {},
    val onError: (Throwable) -> Unit = {},
    val onLoading: () -> Unit = {}
)

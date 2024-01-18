package com.tokopedia.shareexperience.ui.model.arg

import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.ui.uistate.ShareExInitializationUiState

data class ShareExInitializerArg(
    val affiliateEligibilityRequest: ShareExAffiliateEligibilityRequest? = null,
    val onSuccess: (ShareExInitializationUiState) -> Unit = {},
    val onError: (Throwable) -> Unit = {},
    val onLoading: () -> Unit = {}
)

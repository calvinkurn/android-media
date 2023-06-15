package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class UniversalInboxTopAdsBannerUiModel constructor(
    var ads: List<TopAdsImageViewModel>? = null,
    var requested: Boolean = false
) {
    fun hasAds(): Boolean {
        return ads != null
    }
}

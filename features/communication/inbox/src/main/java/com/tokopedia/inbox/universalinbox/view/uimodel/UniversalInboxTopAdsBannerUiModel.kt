package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class UniversalInboxTopAdsBannerUiModel constructor(
    var ads: List<TopAdsImageViewModel>? = null,
    var requested: Boolean = false
) {
    val impressHolder: ImpressHolder = ImpressHolder()

    fun hasAds(): Boolean {
        return ads != null
    }
}

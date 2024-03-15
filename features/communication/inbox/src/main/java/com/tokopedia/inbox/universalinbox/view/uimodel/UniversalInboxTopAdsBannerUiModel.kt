package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

data class UniversalInboxTopAdsBannerUiModel constructor(
        var ads: List<TopAdsImageUiModel>? = null,
        var requested: Boolean = false
) : Visitable<UniversalInboxTypeFactory> {
    override fun type(typeFactory: UniversalInboxTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasAds(): Boolean {
        return ads != null
    }
}

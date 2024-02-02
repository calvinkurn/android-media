package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class UniversalInboxTopAdsVerticalBannerUiModel(var ads: List<TopAdsImageViewModel>? = null, var isRequested: Boolean = false) : Visitable<UniversalInboxTypeFactory> {
    override fun type(typeFactory: UniversalInboxTypeFactory): Int {
        return typeFactory.type(this)
    }
}

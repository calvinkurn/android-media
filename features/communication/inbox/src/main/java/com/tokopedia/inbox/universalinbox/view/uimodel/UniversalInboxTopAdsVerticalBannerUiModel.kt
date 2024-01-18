package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory

data class UniversalInboxTopAdsVerticalBannerUiModel constructor(val isAds:Boolean = false) : Visitable<UniversalInboxTypeFactory> {
    override fun type(typeFactory: UniversalInboxTypeFactory): Int {
        return typeFactory.type(this)
    }
}

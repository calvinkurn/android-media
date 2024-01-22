package com.tokopedia.inbox.universalinbox.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.inbox.universalinbox.view.adapter.typefactory.UniversalInboxTypeFactory

data class UniversalInboxTopAdsVerticalBannerUiModel constructor(var isRequested:Boolean = false) : Visitable<UniversalInboxTypeFactory> {
    override fun type(typeFactory: UniversalInboxTypeFactory): Int {
        return typeFactory.type(this)
    }
}

package com.tokopedia.shareexperience.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

class ShareExAffiliateRegistrationUiModel(
    val icon: String,
    val title: String,
    val description: String,
    val label: String
): Visitable<ShareExTypeFactory> {
    override fun type(typeFactory: ShareExTypeFactory): Int {
        return typeFactory.type(this)
    }
}

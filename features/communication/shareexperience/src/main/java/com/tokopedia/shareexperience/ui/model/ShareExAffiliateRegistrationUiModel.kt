package com.tokopedia.shareexperience.ui.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateRegistrationModel
import com.tokopedia.shareexperience.ui.adapter.typefactory.ShareExTypeFactory

class ShareExAffiliateRegistrationUiModel(
    val affiliateRegistrationModel: ShareExAffiliateRegistrationModel
) : Visitable<ShareExTypeFactory> {

    val impressHolder = ImpressHolder()

    override fun type(typeFactory: ShareExTypeFactory): Int {
        return typeFactory.type(this)
    }
}

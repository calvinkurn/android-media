package com.tokopedia.shareexperience.domain.model.property

import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel

data class ShareExPropertyModel(
    val shareBody: ShareExBodyModel = ShareExBodyModel(),
    val affiliate: ShareExAffiliateModel = ShareExAffiliateModel()
)

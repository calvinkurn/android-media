package com.tokopedia.shareexperience.domain.model.property

import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel

data class ShareExPropertyModel(
    val title: String = "",
    val listImage: List<String> = listOf(),
    val affiliate: ShareExAffiliateModel = ShareExAffiliateModel()
)

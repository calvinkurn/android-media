package com.tokopedia.shareexperience.domain.model.property

import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.property.linkproperties.ShareExLinkProperties

data class ShareExPropertyModel(
    val listImage: List<String> = listOf(),
    val title: String = "",
    val affiliate: ShareExAffiliateModel = ShareExAffiliateModel(),
    val linkProperties: ShareExLinkProperties = ShareExLinkProperties(),
    val imageGenerator: ShareExImageGeneratorPropertyModel? = null
)

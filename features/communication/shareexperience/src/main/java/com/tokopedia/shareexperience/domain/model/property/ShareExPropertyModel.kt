package com.tokopedia.shareexperience.domain.model.property

import com.tokopedia.shareexperience.domain.model.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateModel
import com.tokopedia.shareexperience.domain.model.property.linkproperties.ShareExLinkProperties

data class ShareExPropertyModel(
    val title: String = "",
    val listImage: List<String> = listOf(),
    val affiliate: ShareExAffiliateModel = ShareExAffiliateModel(),
    val linkProperties: ShareExLinkProperties = ShareExLinkProperties(),
    val imageGenerator: ShareExImageGeneratorModel? = null
)

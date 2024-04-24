package com.tokopedia.shareexperience.domain.model.request.shortlink.branch

import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkPropertiesModel

data class ShareExBranchLinkPropertiesRequest(
    val branchUniversalObjectRequest: ShareExBranchUniversalObjectRequest = ShareExBranchUniversalObjectRequest(),
    val linkerPropertiesRequest: ShareExLinkPropertiesModel = ShareExLinkPropertiesModel(),
    val channelEnum: ShareExChannelEnum
)

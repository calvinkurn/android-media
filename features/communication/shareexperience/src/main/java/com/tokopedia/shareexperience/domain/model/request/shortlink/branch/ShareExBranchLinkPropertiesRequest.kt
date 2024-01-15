package com.tokopedia.shareexperience.domain.model.request.shortlink.branch

import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.property.linkproperties.ShareExLinkProperties

data class ShareExBranchLinkPropertiesRequest(
    val branchUniversalObjectRequest: ShareExBranchUniversalObjectRequest = ShareExBranchUniversalObjectRequest(),
    val linkerPropertiesRequest: ShareExLinkProperties = ShareExLinkProperties(),
    val channelEnum: ShareExChannelEnum
)

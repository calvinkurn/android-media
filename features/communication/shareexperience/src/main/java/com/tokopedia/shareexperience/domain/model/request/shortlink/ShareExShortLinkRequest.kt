package com.tokopedia.shareexperience.domain.model.request.shortlink

import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.property.linkproperties.ShareExLinkProperties

data class ShareExShortLinkRequest(
    val identifierId: String,
    val channelEnum: ShareExChannelEnum,
    val linkerPropertiesRequest: ShareExLinkProperties = ShareExLinkProperties(),
    val fallbackPriorityEnumList: List<ShareExShortLinkFallbackPriorityEnum>,
    val defaultUrl: String
)

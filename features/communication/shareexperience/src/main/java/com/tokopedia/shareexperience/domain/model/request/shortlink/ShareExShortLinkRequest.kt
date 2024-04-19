package com.tokopedia.shareexperience.domain.model.request.shortlink

import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.property.ShareExLinkPropertiesModel

data class ShareExShortLinkRequest(
    val identifierId: String,
    val channelEnum: ShareExChannelEnum,
    val pageTypeEnum: ShareExPageTypeEnum,
    val linkerPropertiesRequest: ShareExLinkPropertiesModel = ShareExLinkPropertiesModel(),
    val fallbackPriorityEnumList: List<ShareExShortLinkFallbackPriorityEnum>,
    val defaultUrl: String
)

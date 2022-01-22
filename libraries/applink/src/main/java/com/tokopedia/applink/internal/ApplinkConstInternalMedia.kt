package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalMedia {
    private const val HOST_MEDIA_PICKER = "media-picker"
    const val INTERNAL_MEDIA_PICKER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MEDIA_PICKER"
}
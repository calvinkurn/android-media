package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalMedia {
    private const val HOST_MEDIA_PICKER = "media-picker"
    private const val HOST_MEDIA_PICKER_PREVIEW = "media-picker-preview"

    const val INTERNAL_MEDIA_PICKER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MEDIA_PICKER"
    const val INTERNAL_MEDIA_PICKER_PREVIEW = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_MEDIA_PICKER_PREVIEW"
}
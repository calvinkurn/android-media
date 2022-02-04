package com.tokopedia.applink.internal

import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.INTERNAL_GLOBAL

object ApplinkConstInternalMedia {
    private const val HOST_MEDIA_PICKER = "media-picker"
    private const val HOST_MEDIA_PICKER_PREVIEW = "media-picker-preview"

    const val INTERNAL_MEDIA_PICKER = "$INTERNAL_GLOBAL/$HOST_MEDIA_PICKER"
    const val INTERNAL_MEDIA_PICKER_PREVIEW = "$INTERNAL_GLOBAL/$HOST_MEDIA_PICKER_PREVIEW"
}
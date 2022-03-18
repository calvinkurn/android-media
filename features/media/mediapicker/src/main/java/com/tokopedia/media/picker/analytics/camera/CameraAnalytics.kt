package com.tokopedia.media.picker.analytics.camera

import com.tokopedia.picker.common.PickerPageSource

interface CameraAnalytics {

    fun visitCameraPage(
        entryPoint: PickerPageSource,
        pagePath: String,
        pageType: String
    )

    fun clickRecord(
        entryPoint: PickerPageSource,
    )

}
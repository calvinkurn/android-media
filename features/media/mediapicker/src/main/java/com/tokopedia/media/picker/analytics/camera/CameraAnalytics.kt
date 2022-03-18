package com.tokopedia.media.picker.analytics.camera

interface CameraAnalytics {

    fun visitCameraPage(
        entryPoint: String,
        pagePath: String,
        pageType: String
    )

    fun clickRecord(
        entryPoint: String
    )

}
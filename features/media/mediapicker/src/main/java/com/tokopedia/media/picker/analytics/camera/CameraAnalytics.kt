package com.tokopedia.media.picker.analytics.camera

interface CameraAnalytics {

    fun clickRecord(
        entryPoint: String,
    )

    fun clickShutter(
        entryPoint: String
    )

    fun clickFlash(
        entryPoint: String,
        flashState: String
    )

    fun clickFlip(
        entryPoint: String,
        cameraState: String
    )

    fun clickThumbnail(
        entryPoint: String
    )

    fun clickGalleryTab(
        entryPoint: String
    )

    fun maxPhotoLimit(
        entryPoint: String
    )

    fun maxVideoLimit(
        entryPoint: String
    )

}
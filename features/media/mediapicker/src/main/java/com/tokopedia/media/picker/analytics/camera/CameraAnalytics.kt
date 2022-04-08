package com.tokopedia.media.picker.analytics.camera

interface CameraAnalytics {

    fun clickRecord()

    fun clickShutter()

    fun clickFlash(
        flashState: String
    )

    fun clickFlip(
        cameraState: String
    )

    fun clickThumbnail()

    fun clickGalleryTab()

    fun maxPhotoLimit()

    fun maxVideoLimit()

    fun recordLowStorage()

}
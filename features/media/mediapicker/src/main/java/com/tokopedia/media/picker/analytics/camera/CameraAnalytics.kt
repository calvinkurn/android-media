package com.tokopedia.media.picker.analytics.camera

interface CameraAnalytics {
    fun clickRecord()
    fun clickShutter()
    fun maxPhotoLimit()
    fun maxVideoLimit()
    fun clickThumbnail()
    fun clickGalleryTab()
    fun recordLowStorage()
    fun clickFlash(flashState: String)
    fun clickFlip(cameraState: String)
}
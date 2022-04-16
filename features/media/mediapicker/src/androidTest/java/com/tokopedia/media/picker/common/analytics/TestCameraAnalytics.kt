package com.tokopedia.media.picker.common.analytics

import com.tokopedia.media.picker.analytics.camera.CameraAnalytics

class TestCameraAnalytics : CameraAnalytics {
    override fun clickRecord() {}
    override fun clickShutter() {}
    override fun maxPhotoLimit() {}
    override fun maxVideoLimit() {}
    override fun clickThumbnail() {}
    override fun clickGalleryTab() {}
    override fun recordLowStorage() {}
    override fun clickFlash(flashState: String) {}
    override fun clickFlip(cameraState: String) {}
}
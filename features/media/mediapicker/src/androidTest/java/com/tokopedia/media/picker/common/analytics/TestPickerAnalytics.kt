package com.tokopedia.media.picker.common.analytics

import com.tokopedia.media.picker.analytics.PickerAnalytics
import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics

class TestPickerAnalytics constructor(
    cameraAnalytics: CameraAnalytics,
    galleryAnalytics: GalleryAnalytics
) : PickerAnalytics(cameraAnalytics, galleryAnalytics)
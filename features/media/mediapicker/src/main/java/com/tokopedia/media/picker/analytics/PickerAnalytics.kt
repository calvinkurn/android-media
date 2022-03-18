package com.tokopedia.media.picker.analytics

import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics

class PickerAnalytics(
    cameraAnalytics: CameraAnalytics,
    galleryAnalytics: GalleryAnalytics
) : CameraAnalytics by cameraAnalytics,
    GalleryAnalytics by galleryAnalytics
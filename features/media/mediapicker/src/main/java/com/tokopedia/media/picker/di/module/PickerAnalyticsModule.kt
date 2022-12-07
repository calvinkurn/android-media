package com.tokopedia.media.picker.di.module

import com.tokopedia.media.picker.analytics.camera.CameraAnalytics
import com.tokopedia.media.picker.analytics.camera.CameraAnalyticsImpl
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalytics
import com.tokopedia.media.picker.analytics.gallery.GalleryAnalyticsImpl
import dagger.Binds
import dagger.Module

@Module
abstract class PickerAnalyticsModule {

    @Binds
    internal abstract fun bindGalleryAnalytics(analytics: GalleryAnalyticsImpl): GalleryAnalytics

    @Binds
    internal abstract fun bindCameraAnalytics(analytics: CameraAnalyticsImpl): CameraAnalytics

}
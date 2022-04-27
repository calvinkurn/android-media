package com.tokopedia.media.picker.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.common.analytics.TestPreviewAnalytics
import com.tokopedia.media.preview.analytics.PreviewAnalytics
import com.tokopedia.media.preview.managers.ImageCompressionManager
import com.tokopedia.media.preview.managers.ImageCompressionManagerImpl
import com.tokopedia.media.preview.managers.SaveToGalleryManager
import com.tokopedia.media.preview.managers.SaveToGalleryManagerImpl
import dagger.Module
import dagger.Provides

@Module
object TestPreviewModule {

    @Provides
    @ActivityScope
    fun providePreviewAnalytics(): PreviewAnalytics {
        return TestPreviewAnalytics()
    }

    @Provides
    @ActivityScope
    fun provideImageCompressionManager(
        @ApplicationContext context: Context
    ): ImageCompressionManager {
        return ImageCompressionManagerImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideSaveToGalleryManager(
        @ApplicationContext context: Context
    ): SaveToGalleryManager {
        return SaveToGalleryManagerImpl(context)
    }

}
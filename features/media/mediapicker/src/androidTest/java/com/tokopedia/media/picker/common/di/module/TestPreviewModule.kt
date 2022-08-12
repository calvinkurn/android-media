package com.tokopedia.media.picker.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.picker.common.analytics.TestPreviewAnalytics
import com.tokopedia.media.preview.analytics.PreviewAnalytics
import com.tokopedia.media.preview.data.repository.ImageCompressionRepository
import com.tokopedia.media.preview.data.repository.ImageCompressionRepositoryImpl
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepository
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepositoryImpl
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
    fun provideImageCompressionRepository(
        @ApplicationContext context: Context
    ): ImageCompressionRepository {
        return ImageCompressionRepositoryImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideSaveToGalleryRepository(
        @ApplicationContext context: Context
    ): SaveToGalleryRepository {
        return SaveToGalleryRepositoryImpl(context)
    }

}
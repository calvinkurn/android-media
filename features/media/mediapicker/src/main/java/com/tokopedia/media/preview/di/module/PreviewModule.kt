package com.tokopedia.media.preview.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.preview.managers.ImageCompressionManager
import com.tokopedia.media.preview.managers.ImageCompressionManagerImpl
import com.tokopedia.media.preview.managers.SaveToGalleryManager
import com.tokopedia.media.preview.managers.SaveToGalleryManagerImpl
import dagger.Module
import dagger.Provides

@Module
object PreviewModule {

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
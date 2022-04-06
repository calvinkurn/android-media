package com.tokopedia.media.preview.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.preview.di.scope.PreviewScope
import com.tokopedia.media.preview.managers.ImageCompressionManager
import com.tokopedia.media.preview.managers.ImageCompressionManagerImpl
import com.tokopedia.media.preview.managers.SaveToGalleryManager
import com.tokopedia.media.preview.managers.SaveToGalleryManagerImpl
import com.tokopedia.picker.common.ParamCacheManager
import dagger.Module
import dagger.Provides

@Module
class PreviewModule {

    @Provides
    @PreviewScope
    fun provideParamCacheManager(
        @ApplicationContext context: Context
    ): ParamCacheManager {
        return ParamCacheManager(context)
    }

    @Provides
    @PreviewScope
    fun provideImageCompressionManager(
        @ApplicationContext context: Context
    ): ImageCompressionManager {
        return ImageCompressionManagerImpl(context)
    }

    @Provides
    @PreviewScope
    fun provideSaveToGalleryManager(
        @ApplicationContext context: Context
    ): SaveToGalleryManager {
        return SaveToGalleryManagerImpl(context)
    }

}
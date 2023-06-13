package com.tokopedia.media.preview.di.module

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.preview.analytics.PreviewAnalytics
import com.tokopedia.media.preview.analytics.PreviewAnalyticsImpl
import com.tokopedia.media.preview.data.repository.ImageCompressionRepository
import com.tokopedia.media.preview.data.repository.ImageCompressionRepositoryImpl
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepository
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepositoryImpl
import com.tokopedia.picker.common.cache.PickerCacheManager
import com.tokopedia.picker.common.cache.PickerParamCacheManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object PreviewModule {

    @Provides
    @ActivityScope
    fun providePickerParamCacheManager(
        @ApplicationContext context: Context,
        gson: Gson
    ): PickerCacheManager {
        return PickerParamCacheManager(context, gson)
    }

    @Provides
    @ActivityScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun providePreviewAnalytics(
        userSession: UserSessionInterface,
        paramCacheManager: PickerCacheManager
    ): PreviewAnalytics {
        return PreviewAnalyticsImpl(
            userSession,
            paramCacheManager
        )
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

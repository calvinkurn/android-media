package com.tokopedia.media.preview.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.preview.analytics.PreviewAnalytics
import com.tokopedia.media.preview.analytics.PreviewAnalyticsImpl
import com.tokopedia.media.preview.data.repository.ImageCompressionRepository
import com.tokopedia.media.preview.data.repository.ImageCompressionRepositoryImpl
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepository
import com.tokopedia.media.preview.data.repository.SaveToGalleryRepositoryImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object PreviewModule {

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
        paramCacheManager: ParamCacheManager
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
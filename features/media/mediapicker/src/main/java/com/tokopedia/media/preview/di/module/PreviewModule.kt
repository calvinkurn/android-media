package com.tokopedia.media.preview.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.preview.analytics.PreviewAnalytics
import com.tokopedia.media.preview.analytics.PreviewAnalyticsImpl
import com.tokopedia.media.preview.managers.ImageCompressionManager
import com.tokopedia.media.preview.managers.ImageCompressionManagerImpl
import com.tokopedia.media.preview.managers.SaveToGalleryManager
import com.tokopedia.media.preview.managers.SaveToGalleryManagerImpl
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
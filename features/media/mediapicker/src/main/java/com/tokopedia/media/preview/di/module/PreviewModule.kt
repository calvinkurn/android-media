package com.tokopedia.media.preview.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.picker.di.scope.PickerScope
import com.tokopedia.media.preview.analytics.PreviewAnalytics
import com.tokopedia.media.preview.analytics.PreviewAnalyticsImpl
import com.tokopedia.media.preview.di.scope.PreviewScope
import com.tokopedia.picker.common.ParamCacheManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class PreviewModule {

    @Provides
    @PreviewScope
    fun provideUserSession(
        @ApplicationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @PreviewScope
    fun providePreviewAnalytics(
        userSession: UserSessionInterface,
        paramCacheManager: ParamCacheManager
    ): PreviewAnalytics{
        return PreviewAnalyticsImpl(userSession, paramCacheManager)
    }


    @Provides
    @PreviewScope
    fun provideParamCacheManager(
        @ApplicationContext context: Context
    ): ParamCacheManager {
        return ParamCacheManager(context)
    }

}
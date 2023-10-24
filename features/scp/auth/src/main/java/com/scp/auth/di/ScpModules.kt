package com.scp.auth.di

import android.content.Context
import com.scp.auth.common.analytics.VerificationAnalyticsMapper
import com.scp.auth.verification.VerificationAnalyticsService
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsService
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class ScpModules {

    @ActivityScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideGtm(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }

    @ActivityScope
    @Provides
    fun provideVerifAnalyticsMapper(gtm: ContextAnalytics, userSessionInterface: UserSessionInterface): VerificationAnalyticsMapper {
        return VerificationAnalyticsMapper(gtm, userSessionInterface)
    }

    @ActivityScope
    @Provides
    fun provideVerifAnalyticsService(mapper: VerificationAnalyticsMapper): ScpAnalyticsService {
        return VerificationAnalyticsService(mapper)
    }
}

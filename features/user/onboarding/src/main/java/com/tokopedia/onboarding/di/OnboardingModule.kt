package com.tokopedia.onboarding.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2020-02-09.
 * ade.hadian@tokopedia.com
 */

@OnboardingScope
@Module
class OnboardingModule {

    @OnboardingScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @OnboardingScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @OnboardingScope
    @Provides
    fun provideOnboardingAnalytics(): OnboardingAnalytics = OnboardingAnalytics()
}
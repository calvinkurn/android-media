package com.tokopedia.onboarding.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.onboarding.analytics.OnboardingAnalytics
import com.tokopedia.onboarding.di.OnboardingScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ade Fulki on 2020-02-09.
 * ade.hadian@tokopedia.com
 */

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

    @OnboardingScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @OnboardingScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @OnboardingScope
    @Provides
    fun providerCoroutineDispatchers(): CoroutineDispatchers = CoroutineDispatchersProvider
}
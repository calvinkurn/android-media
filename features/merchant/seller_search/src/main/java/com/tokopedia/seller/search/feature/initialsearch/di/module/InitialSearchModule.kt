package com.tokopedia.seller.search.feature.initialsearch.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.coroutines.dispatcher.CoroutineDispatchers
import com.tokopedia.coroutines.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.seller.search.common.util.GlobalSearchConfig
import com.tokopedia.seller.search.feature.initialsearch.di.scope.InitialSearchScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@InitialSearchScope
@Module(includes = [InitialSearchViewModelModule::class])
class InitialSearchModule {

    @InitialSearchScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @InitialSearchScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @InitialSearchScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl =
            FirebaseRemoteConfigImpl(context)

    @InitialSearchScope
    @Provides
    fun provideGlobalSearchRemoteConfig(remoteConfig: FirebaseRemoteConfigImpl): GlobalSearchConfig {
        return GlobalSearchConfig(remoteConfig)
    }
}
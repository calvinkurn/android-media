package com.tokopedia.emoney.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.emoney.EmoneyAnalytics
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class DigitalEmoneyModule {

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context) : RemoteConfig{
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    fun provideEmoneyAnalytics(): EmoneyAnalytics {
        return EmoneyAnalytics()
    }

    @DigitalEmoneyScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @DigitalEmoneyScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}

package com.tokopedia.home_account.explicitprofile.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.explicitprofile.trackers.ExplicitProfileAnalytics
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
object ExplicitProfileModules {

    @Provides
    @ActivityScope
    @ApplicationContext
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @ActivityScope
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @ActivityScope
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideGraphQlRepository(@ApplicationContext repository: GraphqlRepository): GraphqlRepository {
        return repository
    }

    @Provides
    @ActivityScope
    fun provideMultipleRequestGraphqlUseCase(@ApplicationContext graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @Provides
    @ActivityScope
    fun provideExplicitProfileAnalytics(): ExplicitProfileAnalytics {
        return ExplicitProfileAnalytics()
    }
}
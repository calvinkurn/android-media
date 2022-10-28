package com.tokopedia.media.picker.common.di.common

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.module.net.NetModule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.user.session.datastore.UserSessionDataStoreClient
import dagger.Module
import dagger.Provides

@Module(includes = [NetModule::class])
class TestAppModule(private val context: Context) {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideContext(): Context {
        return context.applicationContext
    }

    @Provides
    @ApplicationScope
    fun provideAbstractionRouter(@ApplicationContext context: Context?): AbstractionRouter {
        return context as AbstractionRouter
    }

    @Provides
    @ApplicationScope
    fun provideGlobalCacheManager(abstractionRouter: AbstractionRouter): CacheManager {
        return abstractionRouter.persistentCacheManager
    }

    @Provides
    @ApplicationScope
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return AndroidTestDispatcherProvider
    }

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCaseInterface {
        return GraphqlUseCase()
    }

    @Provides
    fun provideUserSessionDataStore(@ApplicationContext context: Context): UserSessionDataStore {
        return UserSessionDataStoreClient.getInstance(context)
    }
}
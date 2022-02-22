package com.tokopedia.updateinactivephone.stub.di.base

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.module.net.NetModule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.updateinactivephone.stub.di.CoroutineAndroidTestDispatchersProvider
import dagger.Module
import dagger.Provides

@Module(includes = [NetModule::class])
class FakeAppModule(val context: Context) {

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return context.applicationContext
    }

    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineAndroidTestDispatchersProvider
    }

    @Provides
    @ApplicationContext
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideAbstractionRouter(@ApplicationContext context: Context?): AbstractionRouter {
        return context as AbstractionRouter
    }

    @Provides
    fun provideGlobalCacheManager(abstractionRouter: AbstractionRouter): CacheManager {
        return abstractionRouter.persistentCacheManager
    }

    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCaseInterface {
        return GraphqlUseCase()
    }
}
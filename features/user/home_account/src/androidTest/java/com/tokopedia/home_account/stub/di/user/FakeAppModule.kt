package com.tokopedia.home_account.stub.di.user

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.module.net.NetModule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.home_account.stub.data.GraphqlRepositoryStub
import com.tokopedia.home_account.stub.domain.FakeUserSessionDataStore
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.datastore.UserSessionDataStore
import dagger.Module
import dagger.Provides

@Module(includes = [NetModule::class])
class FakeAppModule(private val context: Context) {
    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return context.applicationContext
    }

    @ApplicationScope
    @Provides
    fun provideUserSessionDataStore(): UserSessionDataStore {
        return FakeUserSessionDataStore()
    }

    @ApplicationScope
    @Provides
    fun provideAbstractionRouter(@ApplicationContext context: Context?): AbstractionRouter {
        return context as AbstractionRouter
    }

    @ApplicationScope
    @Provides
    fun provideGlobalCacheManager(abstractionRouter: AbstractionRouter): CacheManager {
        return abstractionRouter.persistentCacheManager
    }

    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineTestDispatchersProvider
    }

    @ApplicationScope
    @Provides
    @ApplicationContext
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlRepositoryStub()
    }

    @Provides
    fun provideGraphqlUsecase(): GraphqlUseCaseInterface {
        return GraphqlUseCase()
    }
}
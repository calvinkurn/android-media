package com.tokopedia.tokochat.stub.di.base

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
import com.tokopedia.test.application.datastore.TestUserSessionDataStore
import com.tokopedia.tokochat.stub.common.CoroutineAndroidTestDispatchersProvider
import com.tokopedia.tokochat.stub.repository.GraphqlRepositoryStub
import com.tokopedia.user.session.datastore.UserSessionDataStore
import dagger.Module
import dagger.Provides

@Module(includes = [NetModule::class])
class FakeAppModule(private val context: Context) {
    @ApplicationScope
    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return context.applicationContext
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
        return CoroutineAndroidTestDispatchersProvider
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

    @ApplicationScope
    @Provides
    fun provideUserSessionDataStore(): UserSessionDataStore {
        return TestUserSessionDataStore()
    }
}

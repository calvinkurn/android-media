package com.tokopedia.notifcenter.stub.di.base

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
import com.tokopedia.notifcenter.stub.common.CoroutineAndroidTestDispatchersProvider
import com.tokopedia.notifcenter.stub.data.repository.GraphqlRepositoryStub
import com.tokopedia.notifcenter.stub.data.response.GqlResponseStub
import com.tokopedia.test.application.datastore.TestUserSessionDataStore
import com.tokopedia.user.session.datastore.UserSessionDataStore
import dagger.Module
import dagger.Provides

@Module(includes = [NetModule::class])
class NotificationFakeAppModule(private val context: Context) {
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
    @ApplicationContext
    @Provides
    fun provideGqlResponseStub(): GqlResponseStub {
        return GqlResponseStub()
    }

    @ApplicationScope
    @Provides
    @ApplicationContext
    fun provideGraphqlRepository(
        @ApplicationContext gqlResponseStub: GqlResponseStub
    ): GraphqlRepository {
        return GraphqlRepositoryStub(gqlResponseStub)
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

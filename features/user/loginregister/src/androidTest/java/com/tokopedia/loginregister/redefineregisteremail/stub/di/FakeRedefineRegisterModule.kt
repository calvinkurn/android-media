package com.tokopedia.loginregister.redefineregisteremail.stub.di

import android.content.Context
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.module.net.NetModule
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCaseInterface
import com.tokopedia.loginregister.redefineregisteremail.stub.data.RedefineRegisterRepositoryStub
import com.tokopedia.test.application.datastore.TestUserSessionDataStore
import com.tokopedia.user.session.datastore.UserSessionDataStore
import dagger.Module
import dagger.Provides

@Module(includes = [NetModule::class])
class FakeRedefineRegisterModule(private val context: Context) {

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
        return CoroutineDispatchersProvider
    }

    @ApplicationScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCaseInterface {
        return GraphqlUseCase()
    }

    @Provides
    @ApplicationContext
    @ApplicationScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return RedefineRegisterRepositoryStub()
    }

    @ApplicationScope
    @Provides
    fun provideUserSessionDataStore(): UserSessionDataStore {
        return TestUserSessionDataStore()
    }

}

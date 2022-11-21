package com.tokopedia.talk.stub.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.stub.common.coroutine.CoroutineAndroidTestDispatchersProvider
import com.tokopedia.talk.stub.common.graphql.coroutines.domain.repository.GraphqlRepositoryStub
import dagger.Module
import dagger.Provides

@Module
class AppModuleStub(private val context: Context) {
    @ApplicationScope
    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return context.applicationContext
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
}

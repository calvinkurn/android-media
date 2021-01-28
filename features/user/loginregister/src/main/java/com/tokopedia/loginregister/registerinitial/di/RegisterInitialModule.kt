package com.tokopedia.loginregister.registerinitial.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.loginregister.common.DispatcherProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author by nisie on 10/25/18.
 */
@Module
class RegisterInitialModule {
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
        return getInstance().graphqlRepository
    }

    @RegisterInitialScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase {
        return getInstance().multiRequestGraphqlUseCase
    }

    @RegisterInitialScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @RegisterInitialScope
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override fun ui(): CoroutineDispatcher = Dispatchers.Main
        override fun io(): CoroutineDispatcher = Dispatchers.IO
    }
}
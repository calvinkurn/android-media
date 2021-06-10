package com.tokopedia.loginregister.registerinitial.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author by nisie on 10/25/18.
 */
@Module
class RegisterInitialModule {

    @Provides
    fun providesContext(@ApplicationContext context: Context): Context = context

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
}
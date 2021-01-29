package com.tokopedia.entertainment.search.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.entertainment.search.viewmodel.factory.EventSearchViewModelFactory
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Author errysuprayogi on 06,February,2020
 */

@Module
class EventSearchModule {

    @EventSearchScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @EventSearchScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @EventSearchScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @EventSearchScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @EventSearchScope
    @Provides
    fun provideInterceptors(tkpdAuthInterceptor: TkpdAuthInterceptor,
                            loggingInterceptor: HttpLoggingInterceptor,
                            commonErrorResponseInterceptor: CommonErrorResponseInterceptor) =
            mutableListOf(tkpdAuthInterceptor, loggingInterceptor, commonErrorResponseInterceptor)

    @EventSearchScope
    @Provides
    fun provideAuthInterceptors(@ApplicationContext context: Context,
                                userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
    }

    @EventSearchScope
    @Provides
    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

    @EventSearchScope
    @Provides
    fun provideViewModelFactory(dispatcher: CoroutineDispatcher,
                                gqlRepository: GraphqlRepository,
                                userSession: UserSessionInterface):
            EventSearchViewModelFactory = EventSearchViewModelFactory(dispatcher,
            gqlRepository, userSession)

}
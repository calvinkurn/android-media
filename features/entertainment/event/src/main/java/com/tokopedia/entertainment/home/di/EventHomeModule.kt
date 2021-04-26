package com.tokopedia.entertainment.home.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
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
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Author errysuprayogi on 06,February,2020
 */

@Module
class EventHomeModule {

    @EventHomeScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @EventHomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @EventHomeScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @EventHomeScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @EventHomeScope
    @Provides
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository =
            RestRequestInteractor.getInstance().restRepository.apply {
                updateInterceptors(interceptors, context)
            }

    @EventHomeScope
    @Provides
    fun provideInterceptors(tkpdAuthInterceptor: TkpdAuthInterceptor,
                            loggingInterceptor: HttpLoggingInterceptor,
                            commonErrorResponseInterceptor: CommonErrorResponseInterceptor) =
            mutableListOf(tkpdAuthInterceptor, loggingInterceptor, commonErrorResponseInterceptor)

    @EventHomeScope
    @Provides
    fun provideAuthInterceptors(@ApplicationContext context: Context,
                                userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
    }

    @EventHomeScope
    @Provides
    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

    @EventHomeScope
    @Provides
    fun provideTracking(): EventHomePageTracking {
        return EventHomePageTracking()
    }
}
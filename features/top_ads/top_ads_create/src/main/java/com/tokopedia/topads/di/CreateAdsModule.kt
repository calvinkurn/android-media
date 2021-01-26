package com.tokopedia.topads.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.UrlConstant.BASE_REST_URL
import com.tokopedia.topads.UrlConstant.PATH_GROUP_VALIDATE
import com.tokopedia.topads.UrlConstant.PATH_PRODUCT_LIST
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

/**
 * Author errysuprayogi on 08,November,2019
 */

@Module
class CreateAdsModule {

    @CreateAdsScope
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @CreateAdsScope
    @Provides
    @IntoMap
    @StringKey(PATH_PRODUCT_LIST)
    fun provideGroupProductListURL(): String = BASE_REST_URL + PATH_PRODUCT_LIST

    @CreateAdsScope
    @Provides
    @IntoMap
    @StringKey(PATH_GROUP_VALIDATE)
    fun provideGroupValidateURL(): String = BASE_REST_URL + PATH_GROUP_VALIDATE

    @CreateAdsScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @CreateAdsScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @CreateAdsScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @CreateAdsScope
    @Provides
    @Named(UrlConstant.MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @CreateAdsScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatchers = CoroutineDispatchersProvider

    @CreateAdsScope
    @Provides
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository =
            RestRequestInteractor.getInstance().restRepository.apply {
                updateInterceptors(interceptors, context)
            }

    @CreateAdsScope
    @Provides
    fun provideInterceptors(tkpdAuthInterceptor: TkpdAuthInterceptor,
                            loggingInterceptor: HttpLoggingInterceptor,
                            commonErrorResponseInterceptor: CommonErrorResponseInterceptor) =
            mutableListOf(tkpdAuthInterceptor, loggingInterceptor, commonErrorResponseInterceptor)

    @CreateAdsScope
    @Provides
    fun provideAuthInterceptors(@ApplicationContext context: Context,
                                userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
    }

    @CreateAdsScope
    @Provides
    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
        return CommonErrorResponseInterceptor()
    }

}
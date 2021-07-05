package com.tokopedia.deals.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.deals.common.analytics.DealsAnalytics
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by jessica on 11/06/20
 */

@Module
class DealsModule(val context: Context) {

    private val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private val NET_READ_TIMEOUT = 100
    private val NET_WRITE_TIMEOUT = 100
    private val NET_CONNECT_TIMEOUT = 100
    private val NET_RETRY = 3

    @DealsQualifier
    @Provides
    fun provideDealsContext() = context

    @DealsScope
    @Provides
    fun provideDispatcher(): Dispatchers = Dispatchers

    @DealsScope
    @Provides
    fun provideMainDispacther(): CoroutineDispatcher = Dispatchers.Main

    @DealsScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @DealsScope
    @Provides
    fun provideWsV4RetrofitWithErrorHandler(okHttpClient: OkHttpClient,
                                            retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(TokopediaUrl.getInstance().BOOKING).client(okHttpClient).build()
    }

    @DealsScope
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @DealsScope
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

    @DealsScope
    @DealsQualifier
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        if (GlobalConfig.isAllowDebuggingTools()) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }
        return logging
    }

    @Provides
    @DealsScope
    fun provideNetworkRouter(@DealsQualifier context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @DealsScope
    @Provides
    fun provideTkpdAuthInterceptor(@DealsQualifier context: Context,
                                   networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @DealsScope
    @Provides
    fun provideChuckerInterceptor(@DealsQualifier context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @DealsScope
    @Provides
    fun provideRetrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @DealsScope
    @Provides
    fun provideUserSessionInterface(@DealsQualifier context: Context): UserSessionInterface = UserSession(context)

    @DealsScope
    @Provides
    fun provideIrisSession(@DealsQualifier context: Context): IrisSession  = IrisSession(context)

    @DealsScope
    @Provides
    fun provideDealsLocationUtil(@DealsQualifier context: Context): DealsLocationUtils  = DealsLocationUtils(context)

    @DealsScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @DealsScope
    @Provides
    fun provideDealsAnalytics(irisSession: IrisSession, userSessionInterface: UserSessionInterface): DealsAnalytics =
            DealsAnalytics(irisSession, userSessionInterface)
}
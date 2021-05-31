package com.tokopedia.smartbills.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.smartbills.analytics.SmartBillsAnalytics
import com.tokopedia.smartbills.data.api.SmartBillsApi
import com.tokopedia.smartbills.data.api.SmartBillsRepository
import com.tokopedia.smartbills.data.api.SmartBillsRepositoryImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class SmartBillsModule {

    @SmartBillsScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SmartBillsScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SmartBillsScope
    @Provides
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @SmartBillsScope
    @Provides
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @SmartBillsScope
    @Provides
    internal fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor,
                                     digitalInterceptor: DigitalInterceptor,
                                     chuckerInterceptor: ChuckerInterceptor,
                                     okHttpRetryPolicy: OkHttpRetryPolicy): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder
                .addInterceptor(digitalInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chuckerInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .build()
    }

    @SmartBillsScope
    @Provides
    internal fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @SmartBillsScope
    @Provides
    fun provideDigitalInterceptor(@ApplicationContext context: Context,
                                  networkRouter: NetworkRouter,
                                  userSession: UserSessionInterface): DigitalInterceptor {
        return DigitalInterceptor(context, networkRouter, userSession)
    }

    @SmartBillsScope
    @Provides
    fun provideGqlApiService(gson: Gson, client: OkHttpClient): SmartBillsApi {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(DigitalUrl.BASE_URL)
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
        retrofitBuilder.client(client)
        val retrofit = retrofitBuilder.build()
        return retrofit.create(SmartBillsApi::class.java)
    }

    @SmartBillsScope
    @Provides
    fun provideRepository(rechargeCCApi: SmartBillsApi): SmartBillsRepository {
        return SmartBillsRepositoryImpl(rechargeCCApi)
    }

    @SmartBillsScope
    @Provides
    fun provideAnalytics(): SmartBillsAnalytics {
        return SmartBillsAnalytics()
    }

    @Provides
    @SmartBillsScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }
}
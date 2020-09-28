package com.tokopedia.recharge_credit_card.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.di.DigitalCommonScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.Iris
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.recharge_credit_card.analytics.CreditCardAnalytics
import com.tokopedia.recharge_credit_card.data.RechargeCCApi
import com.tokopedia.recharge_credit_card.data.RechargeCCRepository
import com.tokopedia.recharge_credit_card.data.RechargeCCRepositoryImpl
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RechargeCCModule {

    @RechargeCCScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @RechargeCCScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @RechargeCCScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @RechargeCCScope
    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    @RechargeCCScope
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @RechargeCCScope
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    @RechargeCCScope
    internal fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor,
                                     digitalInterceptor: DigitalInterceptor,
                                     chuckInterceptor: ChuckerInterceptor,
                                     okHttpRetryPolicy: OkHttpRetryPolicy): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(digitalInterceptor)
                .addInterceptor(fingerprintInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides
    @RechargeCCScope
    internal fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @RechargeCCScope
    fun provideDigitalInterceptor(@ApplicationContext context: Context,
                                  networkRouter: AbstractionRouter): DigitalInterceptor {
        return DigitalInterceptor(context, networkRouter)
    }

    @Provides
    @RechargeCCScope
    fun provideGqlApiService(gson: Gson, client: OkHttpClient): RechargeCCApi {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(TokopediaUrl.getInstance().PCIDSS_CREDIT_CARD)
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
        retrofitBuilder.client(client)
        val retrofit = retrofitBuilder.build()
        return retrofit.create(RechargeCCApi::class.java)
    }

    @Provides
    @RechargeCCScope
    fun provideRepository(rechargeCCApi: RechargeCCApi): RechargeCCRepository {
        return RechargeCCRepositoryImpl(rechargeCCApi)
    }

    @Provides
    @RechargeCCScope
    fun provideIrisAnalytics(@ApplicationContext context: Context): Iris {
        return IrisAnalytics.getInstance(context)
    }

    @Provides
    @RechargeCCScope
    fun provideAnalytics(iris: Iris): CreditCardAnalytics {
        return CreditCardAnalytics(iris)
    }
}
package com.tokopedia.flight.orderlist.di

import android.content.Context
import android.content.res.Resources
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.flight.orderlist.data.FlightOrderApi
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource
import com.tokopedia.flight.orderlist.di.qualifier.FlightOrderChuckQualifier
import com.tokopedia.flight.orderlist.di.qualifier.FlightOrderQualifier
import com.tokopedia.flight.orderlist.domain.*
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderMapper
import com.tokopedia.flight.orderlist.network.FlightOrderAuthInterceptor
import com.tokopedia.flight.orderlist.network.model.FlightOrderErrorResponse
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author by alvarisi on 12/6/17.
 */
@Module
class FlightOrderModule {

    @FlightOrderScope
    @Provides
    @FlightOrderChuckQualifier
    fun provideChuckInterceptory(@ApplicationContext context: Context): Interceptor {
        return ChuckerInterceptor(context)
    }

    @FlightOrderScope
    @Provides
    fun provideOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                            flightAuthInterceptor: FlightOrderAuthInterceptor,
                            @FlightOrderChuckQualifier chuckInterceptor: Interceptor,
                            @FlightOrderQualifier okHttpRetryPolicy: OkHttpRetryPolicy): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(flightAuthInterceptor)
                .addInterceptor(ErrorResponseInterceptor(FlightOrderErrorResponse::class.java))
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @FlightOrderScope
    @Provides
    @FlightOrderQualifier
    fun provideFlightRetrofit(okHttpClient: OkHttpClient,
                              @FlightOrderQualifier retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(TokopediaUrl.getInstance().API).client(okHttpClient).build()
    }

    @FlightOrderScope
    @FlightOrderQualifier
    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(NET_READ_TIMEOUT, NET_WRITE_TIMEOUT, NET_CONNECT_TIMEOUT, NET_RETRY)
    }

    @FlightOrderScope
    @Provides
    @FlightOrderQualifier
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
    }

    @FlightOrderScope
    @FlightOrderQualifier
    @Provides
    fun provideFlightGson(): Gson {
        return GsonBuilder()
                .setDateFormat(GSON_DATE_FORMAT)
                .setPrettyPrinting()
                .serializeNulls()
                .create()
    }

    @FlightOrderScope
    @Provides
    fun provideFlightOrderApi(@FlightOrderQualifier retrofit: Retrofit): FlightOrderApi {
        return retrofit.create(FlightOrderApi::class.java!!)
    }

    @FlightOrderScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @FlightOrderScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @FlightOrderScope
    @Provides
    fun provideFlightRepository(flightOrderDataSource: FlightOrderDataSource,
                                flightOrderMapper: FlightOrderMapper): FlightOrderRepository {
        return FlightOrderRepositoryImpl(flightOrderDataSource, flightOrderMapper)
    }

    @Provides
    fun provideFlightGetOrdersUseCase(flightOrderRepository: FlightOrderRepository): FlightGetOrdersUseCase {
        return FlightGetOrdersUseCase(flightOrderRepository)
    }

    @Provides
    fun provideFlightGetOrderUseCase(flightOrderRepository: FlightOrderRepository): FlightGetOrderUseCase {
        return FlightGetOrderUseCase(flightOrderRepository)
    }

    @Provides
    fun provideFlightSendEmailUseCase(flightOrderRepository: FlightOrderRepository): FlightSendEmailUseCase {
        return FlightSendEmailUseCase(flightOrderRepository)
    }

    @FlightOrderScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @FlightOrderScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository =
            getInstance().graphqlRepository

    @FlightOrderScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    companion object {
        private const val NET_READ_TIMEOUT = 30
        private const val NET_WRITE_TIMEOUT = 30
        private const val NET_CONNECT_TIMEOUT = 30
        private const val NET_RETRY = 1
        private const val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    }

}

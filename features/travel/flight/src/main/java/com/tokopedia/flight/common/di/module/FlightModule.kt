package com.tokopedia.flight.common.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.common.travel.utils.TrackingCrossSellUtil
import com.tokopedia.config.GlobalConfig
import com.tokopedia.flight.cancellation.data.FlightCancellationCloudDataSource
import com.tokopedia.flight.common.constant.FlightUrl
import com.tokopedia.flight.common.data.model.FlightErrorResponse
import com.tokopedia.flight.common.data.repository.FlightRepositoryImpl
import com.tokopedia.flight.common.data.source.FlightAuthInterceptor
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi
import com.tokopedia.flight.common.data.source.cloud.api.retrofit.StringResponseConverter
import com.tokopedia.flight.common.di.qualifier.FlightChuckQualifier
import com.tokopedia.flight.common.di.qualifier.FlightQualifier
import com.tokopedia.flight.common.di.scope.FlightScope
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.data.FlightOrderApi
import com.tokopedia.flight.orderlist.data.cloud.FlightOrderDataSource
import com.tokopedia.flight.orderlist.domain.FlightGetOrderUseCase
import com.tokopedia.flight.orderlist.domain.model.mapper.FlightOrderMapper
import com.tokopedia.flight.search.data.FlightRouteDao
import com.tokopedia.flight.search.data.cache.db.FlightSearchRoomDb
import com.tokopedia.flight.search.data.cache.db.dao.FlightComboDao
import com.tokopedia.flight.search.data.cache.db.dao.FlightJourneyDao
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.utils.OkHttpRetryPolicy
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
 * @author by furqan on 09/06/2021
 */
@Module
class FlightModule {

    @FlightScope
    @Provides
    @FlightChuckQualifier
    fun provideChuckInterceptor(@ApplicationContext context: Context): Interceptor =
            ChuckerInterceptor(context)

    @FlightScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter =
            context as NetworkRouter

    @FlightScope
    @Provides
    fun provideOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                            flightAuthInterceptor: FlightAuthInterceptor,
                            @FlightChuckQualifier chuckInterceptor: Interceptor,
                            @FlightQualifier okHttpRetryPolicy: OkHttpRetryPolicy): OkHttpClient {
        val builder = OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(flightAuthInterceptor)
                .addInterceptor(ErrorResponseInterceptor(FlightErrorResponse::class.java))

        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @FlightScope
    @FlightQualifier
    @Provides
    fun provideGson(): Gson =
            GsonBuilder()
                    .setDateFormat(GSON_DATE_FORMAT)
                    .setPrettyPrinting()
                    .serializeNulls()
                    .create()

    @FlightScope
    @Provides
    @FlightQualifier
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy =
            OkHttpRetryPolicy(
                    NET_READ_TIMEOUT,
                    NET_WRITE_TIMEOUT,
                    NET_CONNECT_TIMEOUT,
                    NET_RETRY
            )

    @FlightScope
    @FlightQualifier
    @Provides
    fun provideRetrofitBuilder(@FlightQualifier gson: Gson): Retrofit.Builder =
            Retrofit.Builder()
                    .addConverterFactory(StringResponseConverter())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

    @FlightScope
    @Provides
    @FlightQualifier
    fun provideFlightRetrofit(okHttpClient: OkHttpClient,
                              @FlightQualifier retrofitBuilder: Retrofit.Builder): Retrofit =
            retrofitBuilder.baseUrl(FlightUrl.BASE_URL)
                    .client(okHttpClient)
                    .build()

    @FlightScope
    @Provides
    fun provideFlightApi(@FlightQualifier retrofit: Retrofit): FlightApi =
            retrofit.create(FlightApi::class.java)

    @FlightScope
    @Provides
    fun provideFlightOrderApi(@FlightQualifier retrofit: Retrofit): FlightOrderApi =
            retrofit.create(FlightOrderApi::class.java)

    @FlightScope
    @Provides
    fun provideFlightRepository(flightOrderDataSource: FlightOrderDataSource,
                                flightOrderMapper: FlightOrderMapper,
                                flightCancellationCloudDataSource: FlightCancellationCloudDataSource)
            : FlightRepository =
            FlightRepositoryImpl(flightOrderDataSource,
                    flightOrderMapper,
                    flightCancellationCloudDataSource)

    @FlightScope
    @Provides
    fun provideFlightSearchRoomDb(@ApplicationContext context: Context): FlightSearchRoomDb =
            FlightSearchRoomDb.getInstance(context)

    @FlightScope
    @Provides
    fun provideFlightJourneyDao(flightSearchRoomDb: FlightSearchRoomDb): FlightJourneyDao =
            flightSearchRoomDb.flightJourneyDao()

    @FlightScope
    @Provides
    fun provideRouteDao(flightSearchRoomDb: FlightSearchRoomDb): FlightRouteDao =
            flightSearchRoomDb.flightRouteDao()

    @FlightScope
    @Provides
    fun provideComboDao(flightSearchRoomDb: FlightSearchRoomDb): FlightComboDao =
            flightSearchRoomDb.flightComboDao()

    @FlightScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @FlightScope
    @Provides
    fun provideFlightGetOrderUseCase(flightRepository: FlightRepository): FlightGetOrderUseCase =
            FlightGetOrderUseCase(flightRepository)

    @FlightScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @FlightScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @FlightScope
    @Provides
    fun provideTrackingCrossSellUtil(): TrackingCrossSellUtil =
            TrackingCrossSellUtil()

    companion object {
        private const val NET_READ_TIMEOUT: Int = 60
        private const val NET_WRITE_TIMEOUT: Int = 60
        private const val NET_CONNECT_TIMEOUT: Int = 60
        private const val NET_RETRY: Int = 1
        private const val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    }
}
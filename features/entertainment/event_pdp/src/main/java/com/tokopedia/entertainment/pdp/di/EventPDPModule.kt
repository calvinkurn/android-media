package com.tokopedia.entertainment.pdp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.network_api.EventCheckoutApi
import com.tokopedia.entertainment.pdp.network_api.EventCheckoutApi.Companion.BASE_URL
import com.tokopedia.entertainment.pdp.network_api.EventCheckoutRepository
import com.tokopedia.entertainment.pdp.network_api.EventCheckoutRepositoryImpl
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * Author firman on 06-04-20
 */

@EventPDPScope
@Module
class EventPDPModule {

    @EventPDPScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @EventPDPScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @EventPDPScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @EventPDPScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @EventPDPScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @EventPDPScope
    @Provides
    @Named("travel_calendar_holiday_query")
    fun provideTravelCalendarHolidayQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.travelcalendar.R.raw.query_get_travel_calendar_holiday)
    }

    @Provides
    @EventPDPScope
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @EventPDPScope
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    @EventPDPScope
    internal fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @EventPDPScope
    internal fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor,
                                     chuckerInterceptor: ChuckerInterceptor,
                                     okHttpRetryPolicy: OkHttpRetryPolicy): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chuckerInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @EventPDPScope
    fun provideApiService(gson: Gson, client: OkHttpClient): EventCheckoutApi {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
        retrofitBuilder.client(client)
        val retrofit = retrofitBuilder.build()
        return retrofit.create(EventCheckoutApi::class.java)
    }

    @Provides
    @EventPDPScope
    fun provideRepository(eventCheckoutApi: EventCheckoutApi): EventCheckoutRepository {
        return EventCheckoutRepositoryImpl(eventCheckoutApi)
    }

    @Provides
    @EventPDPScope
    fun provideIris(@ApplicationContext  context: Context): IrisSession {
        return IrisSession(context)
    }

    @Provides
    @EventPDPScope
    fun provideTracking(irisSession: IrisSession, userSession: UserSessionInterface): EventPDPTracking {
        return EventPDPTracking(userSession, irisSession)
    }

    @Provides
    @EventPDPScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

}
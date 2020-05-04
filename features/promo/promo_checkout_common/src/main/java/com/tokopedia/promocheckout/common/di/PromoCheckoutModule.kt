package com.tokopedia.promocheckout.common.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.promocheckout.common.domain.CancelPromoUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeFinalUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.event.network_api.EventCheckoutApi
import com.tokopedia.promocheckout.common.domain.event.network_api.EventCheckoutApi.Companion.BASE_URL_EVENT
import com.tokopedia.promocheckout.common.domain.event.repository.EventCheckRepository
import com.tokopedia.promocheckout.common.domain.event.repository.EventCheckRepositoryImpl
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.umroh.UmrahCheckPromoUseCase
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

@Module
class PromoCheckoutModule {

    @PromoCheckoutQualifier
    @Provides
    fun provideCancelPromoUseCase(@PromoCheckoutQualifier listInterceptor : ArrayList<Interceptor>, @ApplicationContext context: Context): CancelPromoUseCase {
        return CancelPromoUseCase(listInterceptor, context)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoCodeUseCase(@ApplicationContext context: Context): CheckPromoCodeUseCase {
        return CheckPromoCodeUseCase(context.resources, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoStackingCodeFinalUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeFinalUseCase {
        return CheckPromoStackingCodeFinalUseCase(context)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckVoucherDigitalUseCase(@ApplicationContext context: Context): DigitalCheckVoucherUseCase {
        return DigitalCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckVoucherFlightUseCase(@ApplicationContext context: Context): FlightCheckVoucherUseCase {
        return FlightCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCancelVoucherDigitalUseCase(@ApplicationContext context: Context): FlightCancelVoucherUseCase {
        return FlightCancelVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideUmrahCheckPromoUseCase(@ApplicationContext context: Context): UmrahCheckPromoUseCase {
        return UmrahCheckPromoUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, @PromoCheckoutQualifier networkRouter: NetworkRouter,
                                   @PromoCheckoutQualifier userSession: UserSession) : ArrayList<Interceptor>{
        val  listInterceptor =  ArrayList<Interceptor>()
        listInterceptor.add(TkpdAuthInterceptor(context, networkRouter, userSession))
        if(GlobalConfig.DEBUG){
            listInterceptor.add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return listInterceptor
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context) : NetworkRouter{
        return (context as NetworkRouter)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideUserSession(@ApplicationContext context: Context) : UserSession {
        val userSession = UserSession(context)
        return userSession
    }

    @Provides
    @PromoCheckoutQualifier
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @PromoCheckoutQualifier
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    @PromoCheckoutQualifier
    internal fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor,
                                     okHttpRetryPolicy: OkHttpRetryPolicy): OkHttpClient {
        val builder = OkHttpClient.Builder()
        return builder
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @PromoCheckoutQualifier
    fun provideApiService(gson: Gson, client: OkHttpClient): EventCheckoutApi {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(BASE_URL_EVENT)
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        retrofitBuilder.client(client)
        val retrofit = retrofitBuilder.build()
        return retrofit.create(EventCheckoutApi::class.java)
    }

    @Provides
    @PromoCheckoutQualifier
    fun provideRepository(eventCheckoutApi: EventCheckoutApi): EventCheckRepository {
        return EventCheckRepositoryImpl(eventCheckoutApi)
    }

}
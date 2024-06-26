package com.tokopedia.promocheckout.detail.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.deals.DealsCheckRepositoryImpl
import com.tokopedia.promocheckout.common.domain.deals.DealsCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.deals.DealsCheckoutApi
import com.tokopedia.promocheckout.common.domain.deals.PromoCheckoutDealsRepository
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.event.network_api.EventCheckoutApi
import com.tokopedia.promocheckout.common.domain.event.repository.EventCheckRepository
import com.tokopedia.promocheckout.common.domain.event.repository.EventCheckRepositoryImpl
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.common.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.view.presenter.*
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

@Module(includes = arrayOf(PromoCheckoutModule::class))
class PromoCheckoutDetailModule {

    @PromoCheckoutDetailScope
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideDigitalCheckVoucherUseCase(@ApplicationContext context: Context): DigitalCheckVoucherUseCase {
        return DigitalCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideFlightCheckVoucherUseCase(): FlightCheckVoucherUseCase {
        return FlightCheckVoucherUseCase(GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideFlightCancelVoucherUseCase(): FlightCancelVoucherUseCase {
        return FlightCancelVoucherUseCase(GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideHotelCheckVoucherUseCase(@ApplicationContext context: Context): HotelCheckVoucherUseCase {
        return HotelCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideMarketplacePresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                    checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase): PromoCheckoutDetailPresenter {
        return PromoCheckoutDetailPresenter(getDetailCouponMarketplaceUseCase, checkPromoStackingCodeUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideDigitalPresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase,
                                digitalCheckVoucherMapper: DigitalCheckVoucherMapper): PromoCheckoutDetailDigitalPresenter {
        return PromoCheckoutDetailDigitalPresenter(getDetailCouponMarketplaceUseCase, digitalCheckVoucherUseCase, digitalCheckVoucherMapper)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideFlightPresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                               flightCheckVoucherUseCase: FlightCheckVoucherUseCase,
                               flightCheckVoucherMapper: FlightCheckVoucherMapper,
                               flightCancelVoucherUseCase: FlightCancelVoucherUseCase): PromoCheckoutDetailFlightPresenter {
        return PromoCheckoutDetailFlightPresenter(getDetailCouponMarketplaceUseCase, flightCheckVoucherUseCase, flightCheckVoucherMapper, flightCancelVoucherUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideHotelPresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                              hotelCheckVoucherUseCase: HotelCheckVoucherUseCase,
                              hotelCheckVoucherMapper: HotelCheckVoucherMapper,
                              flightCancelVoucherUseCase: FlightCancelVoucherUseCase): PromoCheckoutDetailHotelPresenter {
        return PromoCheckoutDetailHotelPresenter(getDetailCouponMarketplaceUseCase, hotelCheckVoucherUseCase, hotelCheckVoucherMapper, flightCancelVoucherUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideEventPresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                              eventCheckRepository: EventCheckRepository,
                              compositeSubscription: CompositeSubscription): PromoCheckoutDetailEventPresenter {
        return PromoCheckoutDetailEventPresenter(getDetailCouponMarketplaceUseCase, eventCheckRepository, compositeSubscription)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideDetailDealsPresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                    dealsCheckVoucherUseCase: DealsCheckVoucherUseCase
    ): PromoCheckoutDetailDealsPresenter {
        return PromoCheckoutDetailDealsPresenter(getDetailCouponMarketplaceUseCase, dealsCheckVoucherUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideGetDetailMarketplaceUseCase(@ApplicationContext context: Context): GetDetailCouponMarketplaceUseCase {
        return GetDetailCouponMarketplaceUseCase(context.resources)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideDealsPromoUseCase(): DealsCheckVoucherUseCase {
        return DealsCheckVoucherUseCase(GraphqlUseCase())
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideTrackingPromo(@ApplicationContext context: Context): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @Provides
    @PromoCheckoutDetailScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        val userSession = UserSession(context)
        return userSession
    }

    @Provides
    @PromoCheckoutDetailScope
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @PromoCheckoutDetailScope
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    @PromoCheckoutDetailScope
    fun provideTkpdOldAuthInterceptor(@ApplicationContext context: Context,
                                   userSession: UserSessionInterface): TkpdOldAuthInterceptor {
        return TkpdOldAuthInterceptor(context, context.applicationContext as NetworkRouter, userSession)
    }

    @Provides
    @PromoCheckoutDetailScope
    internal fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor,
                                     chuckerInterceptor: ChuckerInterceptor,
                                     okHttpRetryPolicy: OkHttpRetryPolicy,
                                     tkpdOldAuthInterceptor: TkpdOldAuthInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if(GlobalConfig.isAllowDebuggingTools()){
            builder.addInterceptor(chuckerInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
        }
        return builder
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdOldAuthInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @PromoCheckoutDetailScope
    fun provideApiService(gson: Gson, client: OkHttpClient): EventCheckoutApi {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(EventCheckoutApi.BASE_URL_EVENT)
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        retrofitBuilder.client(client)
        val retrofit = retrofitBuilder.build()
        return retrofit.create(EventCheckoutApi::class.java)
    }

    @Provides
    @PromoCheckoutDetailScope
    fun provideDealsApiService(gson: Gson, client: OkHttpClient): DealsCheckoutApi {
        val retrofitBuilder = Retrofit.Builder()
                .baseUrl(EventCheckoutApi.BASE_URL_EVENT)
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        retrofitBuilder.client(client)
        val retrofit = retrofitBuilder.build()
        return retrofit.create(DealsCheckoutApi::class.java)
    }

    @Provides
    @PromoCheckoutDetailScope
    fun provideRepository(eventCheckoutApi: EventCheckoutApi): EventCheckRepository {
        return EventCheckRepositoryImpl(eventCheckoutApi)
    }

    @Provides
    @PromoCheckoutDetailScope
    fun provideDealsRepository(dealsCheckoutApi: DealsCheckoutApi): PromoCheckoutDealsRepository {
        return DealsCheckRepositoryImpl(dealsCheckoutApi)
    }


    @Provides
    @PromoCheckoutDetailScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }
}

package com.tokopedia.promocheckout.list.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.deals.DealsCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.event.network_api.EventCheckoutApi
import com.tokopedia.promocheckout.common.domain.event.repository.EventCheckRepository
import com.tokopedia.promocheckout.common.domain.event.repository.EventCheckRepositoryImpl
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.hotel.HotelCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.*
import com.tokopedia.promocheckout.common.domain.umroh.UmrahCheckPromoUseCase
import com.tokopedia.promocheckout.list.view.presenter.*
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
class PromoCheckoutListModule {

    @PromoCheckoutListScope
    @Provides
    fun providePresenter(): PromoCheckoutListPresenter {
        return PromoCheckoutListPresenter(GraphqlUseCase(), GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideDigitalCheckVoucherUseCase(@ApplicationContext context: Context): DigitalCheckVoucherUseCase {
        return DigitalCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideFlightCheckVoucherUseCase(): FlightCheckVoucherUseCase {
        return FlightCheckVoucherUseCase(GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideHotelCheckVoucherUseCase(@ApplicationContext context: Context): HotelCheckVoucherUseCase {
        return HotelCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideUmrahCheckPromoUseCase(@ApplicationContext context: Context): UmrahCheckPromoUseCase {
        return UmrahCheckPromoUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideDealsPromoUseCase(): DealsCheckVoucherUseCase {
        return DealsCheckVoucherUseCase(GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideMarketplacePresenter(graphqlUseCase: GraphqlUseCase, checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase): PromoCheckoutListMarketplacePresenter {
        return PromoCheckoutListMarketplacePresenter(graphqlUseCase, checkPromoStackingCodeUseCase)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideDigitalPresenter(digitalCheckVoucherUseCase: DigitalCheckVoucherUseCase, digitalCheckVoucherMapper: DigitalCheckVoucherMapper): PromoCheckoutListDigitalPresenter {
        return PromoCheckoutListDigitalPresenter(digitalCheckVoucherUseCase, digitalCheckVoucherMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideFlightPresenter(flightCheckVoucherUseCase: FlightCheckVoucherUseCase,
                               flightCheckVoucherMapper: FlightCheckVoucherMapper): PromoCheckoutListFlightPresenter {
        return PromoCheckoutListFlightPresenter(flightCheckVoucherUseCase, flightCheckVoucherMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideHotelPresenter(hotelCheckVoucherUseCase: HotelCheckVoucherUseCase,
                              hotelCheckVoucherMapper: HotelCheckVoucherMapper) : PromoCheckoutListHotelPresenter {
        return PromoCheckoutListHotelPresenter(hotelCheckVoucherUseCase, hotelCheckVoucherMapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideDealsPresenter(promoCheckoutListDealsUseCase: DealsCheckVoucherUseCase, graphqlUseCase: GraphqlUseCase): PromoCheckoutListDealsPresenter {
        return PromoCheckoutListDealsPresenter(promoCheckoutListDealsUseCase, graphqlUseCase)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideUmrahPresenter(umrahCheckPromoUseCase: UmrahCheckPromoUseCase,
                              umrahCheckPromoMapper: UmrahCheckPromoMapper) : PromoCheckoutListUmrahPresenter {
        return PromoCheckoutListUmrahPresenter(umrahCheckPromoUseCase, umrahCheckPromoMapper)
    }

    @Provides
    @PromoCheckoutListScope
    fun provideRepository(eventCheckoutApi: EventCheckoutApi): EventCheckRepository {
        return EventCheckRepositoryImpl(eventCheckoutApi)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideEventPresenter(eventCheckRepository: EventCheckRepository,
                              compositeSubscription: CompositeSubscription
    ):PromoCheckoutListEventPresenter{
        return PromoCheckoutListEventPresenter(eventCheckRepository, compositeSubscription)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideTrackingPromo(@ApplicationContext context: Context): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }


    @Provides
    @PromoCheckoutListScope
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @PromoCheckoutListScope
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }


    @Provides
    @PromoCheckoutListScope
    fun provideUserSession(@ApplicationContext context: Context) : UserSessionInterface {
        val userSession = UserSession(context)
        return userSession
    }

    @Provides
    @PromoCheckoutListScope
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context.applicationContext as NetworkRouter, userSession)
    }

    @Provides
    @PromoCheckoutListScope
    internal fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor,
                                     tkpdAuthInterceptor: TkpdAuthInterceptor,
                                     chuckerInterceptor: ChuckerInterceptor,
                                     okHttpRetryPolicy: OkHttpRetryPolicy): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if(GlobalConfig.isAllowDebuggingTools()){
            builder.addInterceptor(chuckerInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
        }
        return builder
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(tkpdAuthInterceptor)
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .build()
    }

    @PromoCheckoutListScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context) : NetworkRouter{
        return (context as NetworkRouter)
    }

    @Provides
    @PromoCheckoutListScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @PromoCheckoutListScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

}

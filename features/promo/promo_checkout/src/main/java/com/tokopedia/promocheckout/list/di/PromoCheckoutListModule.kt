package com.tokopedia.promocheckout.list.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.mapper.*
import com.tokopedia.promocheckout.common.domain.umroh.UmrahCheckPromoUseCase
import com.tokopedia.promocheckout.list.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.list.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.list.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.list.view.presenter.*
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
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

@Module(includes = arrayOf(PromoCheckoutModule::class))
class PromoCheckoutListModule {
    @PromoCheckoutListScope
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideUmrahCheckPromoUseCase(@ApplicationContext context: Context): UmrahCheckPromoUseCase {
        return UmrahCheckPromoUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutListScope
    @Provides
    fun provideMarketplacePresenter(graphqlUseCase: GraphqlUseCase, checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase): PromoCheckoutListMarketplacePresenter {
        return PromoCheckoutListMarketplacePresenter(graphqlUseCase, checkPromoStackingCodeUseCase)
    }

    @PromoCheckoutListScope
    @Provides
    fun provideUmrahPresenter(umrahCheckPromoUseCase: UmrahCheckPromoUseCase,
                              umrahCheckPromoMapper: UmrahCheckPromoMapper) : PromoCheckoutListUmrahPresenter {
        return PromoCheckoutListUmrahPresenter(umrahCheckPromoUseCase, umrahCheckPromoMapper)
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

    @Provides
    @PromoCheckoutListScope
    fun provideDigitalCheckVoucherMapper(): DigitalCheckVoucherMapper = DigitalCheckVoucherMapper()

    @Provides
    @PromoCheckoutListScope
    fun provideFlightCheckVoucherMapper(): FlightCheckVoucherMapper = FlightCheckVoucherMapper()

    @Provides
    @PromoCheckoutListScope
    fun provideHotelCheckVoucherMapper(): HotelCheckVoucherMapper = HotelCheckVoucherMapper()

    @Provides
    @PromoCheckoutListScope
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @PromoCheckoutListScope
    fun provideRestRepository(interceptors: ArrayList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @Provides
    @PromoCheckoutListScope
    fun provideDealsInterceptor(tkpdAuthInterceptor: TkpdAuthInterceptor,
                                fingerprintInterceptor: FingerprintInterceptor,
                                httpLoggingInterceptor: HttpLoggingInterceptor,
                                chuckerInterceptor: ChuckerInterceptor): ArrayList<Interceptor> {
        val listInterceptor = arrayListOf<Interceptor>()
        listInterceptor.add(tkpdAuthInterceptor)
        listInterceptor.add(fingerprintInterceptor)
        if (GlobalConfig.isAllowDebuggingTools()){
            listInterceptor.add(httpLoggingInterceptor)
            listInterceptor.add(chuckerInterceptor)
        }
        return listInterceptor
    }
}

package com.tokopedia.promocheckout.detail.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.list.domain.mapper.DigitalCheckVoucherMapper
import com.tokopedia.promocheckout.list.domain.mapper.FlightCheckVoucherMapper
import com.tokopedia.promocheckout.list.domain.mapper.HotelCheckVoucherMapper
import com.tokopedia.promocheckout.detail.domain.GetDetailCouponMarketplaceUseCase
import com.tokopedia.promocheckout.detail.view.presenter.*
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
class PromoCheckoutDetailModule {

    @PromoCheckoutDetailScope
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideMarketplacePresenter(getDetailCouponMarketplaceUseCase: GetDetailCouponMarketplaceUseCase,
                                    checkPromoStackingCodeUseCase: CheckPromoStackingCodeUseCase,
                                    clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase): PromoCheckoutDetailPresenter {
        return PromoCheckoutDetailPresenter(getDetailCouponMarketplaceUseCase, checkPromoStackingCodeUseCase, clearCacheAutoApplyStackUseCase)
    }

    @PromoCheckoutDetailScope
    @Provides
    fun provideGetDetailMarketplaceUseCase(@ApplicationContext context: Context): GetDetailCouponMarketplaceUseCase {
        return GetDetailCouponMarketplaceUseCase(context.resources)
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
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context.applicationContext as NetworkRouter, userSession)
    }

    @Provides
    @PromoCheckoutDetailScope
    internal fun provideOkHttpClient(fingerprintInterceptor: FingerprintInterceptor,
                                     httpLoggingInterceptor: HttpLoggingInterceptor,
                                     chuckerInterceptor: ChuckerInterceptor,
                                     okHttpRetryPolicy: OkHttpRetryPolicy,
                                     tkpdAuthInterceptor: TkpdAuthInterceptor): OkHttpClient {
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

    @Provides
    @PromoCheckoutDetailScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @PromoCheckoutDetailScope
    fun provideDigitalCheckVoucherMapper(): DigitalCheckVoucherMapper = DigitalCheckVoucherMapper()

    @Provides
    @PromoCheckoutDetailScope
    fun provideFlightCheckVoucherMapper(): FlightCheckVoucherMapper = FlightCheckVoucherMapper()

    @Provides
    @PromoCheckoutDetailScope
    fun provideHotelCheckVoucherMapper(): HotelCheckVoucherMapper = HotelCheckVoucherMapper()

    @Provides
    @PromoCheckoutDetailScope
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @PromoCheckoutDetailScope
    fun provideRestRepository(interceptors: ArrayList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @Provides
    @PromoCheckoutDetailScope
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

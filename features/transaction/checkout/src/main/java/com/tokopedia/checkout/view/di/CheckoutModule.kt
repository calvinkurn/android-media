package com.tokopedia.checkout.view.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.authentication.AuthHelper.Companion.getUserAgent
import com.tokopedia.checkout.R
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.api.*
import com.tokopedia.checkout.data.repository.CheckoutRepository
import com.tokopedia.checkout.data.repository.CommonPurchaseRepository
import com.tokopedia.checkout.data.repository.ICheckoutRepository
import com.tokopedia.checkout.data.repository.ICommonPurchaseRepository
import com.tokopedia.checkout.domain.mapper.CheckoutMapper
import com.tokopedia.checkout.domain.mapper.ICheckoutMapper
import com.tokopedia.checkout.domain.mapper.IShipmentMapper
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormGqlUseCase.Companion.SHIPMENT_ADDRESS_FORM_QUERY
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.config.GlobalConfig
import com.tokopedia.logisticcart.domain.executor.MainScheduler
import com.tokopedia.logisticcart.domain.executor.SchedulerProvider
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.analytics.CodAnalytics
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.di.PromoCheckoutModule
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.di.PurchasePlatformAkamaiQualifier
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformQualifier
import com.tokopedia.purchase_platform.common.feature.editaddress.di.PeopleAddressNetworkModule
import com.tokopedia.purchase_platform.common.feature.editaddress.domain.usecase.EditAddressUseCase
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.insurance.InsuranceItemActionListener
import com.tokopedia.purchase_platform.common.feature.insurance.usecase.GetInsuranceCartUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.url.TokopediaUrl
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
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-26.
 */

@Module(includes = [
    PeopleAddressNetworkModule::class,
    PromoCheckoutModule::class,
    PurchasePlatformNetworkModule::class,
    PurchasePlatformBaseModule::class
])
class CheckoutModule constructor(val shipmentFragment: ShipmentFragment) {

    @Provides
    @CheckoutScope
    fun getCartApiInterceptor(@ApplicationContext context: Context, userSessionInterface: UserSessionInterface): CartApiInterceptor {
        return CartApiInterceptor(context, context as NetworkRouter, userSessionInterface, CommonPurchaseApiUrl.HMAC_KEY)
    }

    @Provides
    @PurchasePlatformQualifier
    @CheckoutScope
    fun provideCartApiOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                   cartApiInterceptor: CartApiInterceptor,
                                   okHttpRetryPolicy: OkHttpRetryPolicy,
                                   fingerprintInterceptor: FingerprintInterceptor,
                                   chuckInterceptor: ChuckerInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                    newRequest.addHeader("User-Agent", getUserAgent())
                    chain.proceed(newRequest.build())
                }
                .addInterceptor(cartApiInterceptor)
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides
    @PurchasePlatformAkamaiQualifier
    @CheckoutScope
    fun provideCartAkamaiApiOkHttpClient(
            @ApplicationContext context: Context,
            @ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
            cartApiInterceptor: CartApiInterceptor,
            okHttpRetryPolicy: OkHttpRetryPolicy,
            fingerprintInterceptor: FingerprintInterceptor,
            chuckInterceptor: ChuckerInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                    newRequest.addHeader("User-Agent", getUserAgent())
                    chain.proceed(newRequest.build())
                }
                .addInterceptor(cartApiInterceptor)
        builder.addInterceptor(AkamaiBotInterceptor(context))
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides
    @PurchasePlatformQualifier
    @CheckoutScope
    fun provideCartApiRetrofit(@PurchasePlatformQualifier okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(TokopediaUrl.getInstance().API)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @PurchasePlatformAkamaiQualifier
    @CheckoutScope
    fun provideCartAkamaiApiRetrofit(@PurchasePlatformAkamaiQualifier okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(TokopediaUrl.getInstance().API)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    @CheckoutScope
    fun provideCommonPurchaseApi(@PurchasePlatformQualifier retrofit: Retrofit): CommonPurchaseApi {
        return retrofit.create(CommonPurchaseApi::class.java)
    }

    @Provides
    @CheckoutScope
    fun provideCommonPurchaseAkamaiApi(@PurchasePlatformAkamaiQualifier retrofit: Retrofit): CommonPurchaseAkamaiApi {
        return retrofit.create(CommonPurchaseAkamaiApi::class.java)
    }

    @Provides
    @CheckoutScope
    fun provideCommonPurchaseRepository(commonPurchaseApi: CommonPurchaseApi, commonPurchaseAkamaiApi: CommonPurchaseAkamaiApi): ICommonPurchaseRepository {
        return CommonPurchaseRepository(commonPurchaseApi, commonPurchaseAkamaiApi)
    }

    @Provides
    @CheckoutScope
    fun provideICheckoutMapper(): ICheckoutMapper {
        return CheckoutMapper()
    }

    @Provides
    @CheckoutScope
    fun provideCheckoutApi(@PurchasePlatformQualifier retrofit: Retrofit): CheckoutApi {
        return retrofit.create(CheckoutApi::class.java)
    }

    @Provides
    @CheckoutScope
    fun provideICheckoutRepository(checkoutApi: CheckoutApi): ICheckoutRepository {
        return CheckoutRepository(checkoutApi)
    }

    @Provides
    @CheckoutScope
    fun provideIShipmentMapper(): IShipmentMapper {
        return ShipmentMapper()
    }

    @Provides
    @CheckoutScope
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @Provides
    @CheckoutScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @CheckoutScope
    @Named(SubmitHelpTicketUseCase.QUERY_NAME)
    fun provideSubmitHelpTicketUseCaseQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.purchase_platform.common.R.raw.submit_help_ticket)
    }

    @Provides
    @CheckoutScope
    fun provideScheduler(): SchedulerProvider {
        return MainScheduler()
    }

    @Provides
    @CheckoutScope
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

    @Provides
    @CheckoutScope
    fun provideShipmentPresenter(compositeSubscription: CompositeSubscription,
                                 checkoutUseCase: CheckoutUseCase,
                                 getShipmentAddressFormGqlUseCase: GetShipmentAddressFormGqlUseCase,
                                 editAddressUseCase: EditAddressUseCase,
                                 changeShippingAddressUseCase: ChangeShippingAddressUseCase,
                                 saveShipmentStateUseCase: SaveShipmentStateUseCase,
                                 codCheckoutUseCase: CodCheckoutUseCase,
                                 ratesUseCase: GetRatesUseCase,
                                 ratesApiUseCase: GetRatesApiUseCase,
                                 stateConverter: RatesResponseStateConverter,
                                 clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                 submitHelpTicketUseCase: SubmitHelpTicketUseCase,
                                 shippingCourierConverter: ShippingCourierConverter,
                                 userSessionInterface: UserSessionInterface,
                                 analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection,
                                 codAnalytics: CodAnalytics,
                                 checkoutAnalytics: CheckoutAnalyticsCourierSelection,
                                 getInsuranceCartUseCase: GetInsuranceCartUseCase,
                                 shipmentDataConverter: ShipmentDataConverter,
                                 releaseBookingUseCase: ReleaseBookingUseCase,
                                 validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase): ShipmentContract.Presenter {
        return ShipmentPresenter(compositeSubscription,
                checkoutUseCase, getShipmentAddressFormGqlUseCase,
                editAddressUseCase, changeShippingAddressUseCase,
                saveShipmentStateUseCase,
                ratesUseCase, ratesApiUseCase,
                codCheckoutUseCase, clearCacheAutoApplyStackUseCase, submitHelpTicketUseCase,
                stateConverter, shippingCourierConverter, shipmentFragment, userSessionInterface,
                analyticsPurchaseProtection, codAnalytics, checkoutAnalytics, getInsuranceCartUseCase,
                shipmentDataConverter, releaseBookingUseCase, validateUsePromoRevampUseCase)
    }

    @Provides
    @CheckoutScope
    fun provideShipmentAdapterActionListener(): ShipmentAdapterActionListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun provideInsuranceItemActionListener(): InsuranceItemActionListener {
        return shipmentFragment
    }

    @Provides
    @CheckoutScope
    fun provideTrackingPromo(): TrackingPromoCheckoutUtil {
        return TrackingPromoCheckoutUtil()
    }

    @Provides
    @CheckoutScope
    @Named(SHIPMENT_ADDRESS_FORM_QUERY)
    fun provideGetShipmentAddressFormQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.shipment_address_form_query)
    }

}
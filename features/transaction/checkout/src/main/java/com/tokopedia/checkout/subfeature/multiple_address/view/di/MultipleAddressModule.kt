package com.tokopedia.checkout.subfeature.multiple_address.view.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.checkout.R
import com.tokopedia.checkout.data.api.CartApiInterceptor
import com.tokopedia.checkout.data.api.CartResponseConverter
import com.tokopedia.checkout.data.api.CommonPurchaseApiUrl
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.subfeature.multiple_address.data.api.MultipleAddressApi
import com.tokopedia.checkout.subfeature.multiple_address.data.repository.IMultipleAddressRepository
import com.tokopedia.checkout.subfeature.multiple_address.data.repository.MultipleAddressRepository
import com.tokopedia.checkout.subfeature.multiple_address.domain.mapper.CartMapper
import com.tokopedia.checkout.subfeature.multiple_address.domain.mapper.ICartMapper
import com.tokopedia.checkout.subfeature.multiple_address.domain.usecase.GetCartMultipleAddressListUseCase
import com.tokopedia.checkout.subfeature.multiple_address.view.IMultipleAddressPresenter
import com.tokopedia.checkout.subfeature.multiple_address.view.MultipleAddressPresenter
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.purchase_platform.common.di.PurchasePlatformAkamaiQualifier
import com.tokopedia.purchase_platform.common.di.PurchasePlatformBaseModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformNetworkModule
import com.tokopedia.purchase_platform.common.di.PurchasePlatformQualifier
import com.tokopedia.purchase_platform.common.schedulers.DefaultSchedulers
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@Module(includes = [
    PurchasePlatformNetworkModule::class,
    PurchasePlatformBaseModule::class
])
class MultipleAddressModule {

    @Provides
    @MultipleAddressScope
    fun getCartApiInterceptor(@ApplicationContext context: Context, userSessionInterface: UserSessionInterface): CartApiInterceptor {
        return CartApiInterceptor(context, context as NetworkRouter, userSessionInterface, CommonPurchaseApiUrl.HMAC_KEY)
    }

    @Provides
    @PurchasePlatformQualifier
    @MultipleAddressScope
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
                    newRequest.addHeader("User-Agent", AuthHelper.getUserAgent())
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
    @MultipleAddressScope
    fun provideAkamaiRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @PurchasePlatformQualifier
    @MultipleAddressScope
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
    @MultipleAddressScope
    fun provideMultipleAddressApi(@PurchasePlatformQualifier retrofit: Retrofit): MultipleAddressApi {
        return retrofit.create(MultipleAddressApi::class.java)
    }

    @Provides
    @MultipleAddressScope
    fun provideIMultipleAddressRepository(multipleAddressApi: MultipleAddressApi): IMultipleAddressRepository {
        return MultipleAddressRepository(multipleAddressApi)
    }

    @Provides
    @MultipleAddressScope
    fun provideICartMapper(): ICartMapper {
        return CartMapper()
    }

    @Provides
    @MultipleAddressScope
    fun provideExecutorSchedulers(): ExecutorSchedulers = DefaultSchedulers

    @Provides
    @MultipleAddressScope
    @Named(ChangeShippingAddressGqlUseCase.CHANGE_SHIPPING_ADDRESS_MUTATION)
    fun provideChangeShippingAddressMutation(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.change_shipping_address_mutation)
    }

    @Provides
    @MultipleAddressScope
    fun providePresenter(changeShippingAddressUseCase: ChangeShippingAddressGqlUseCase,
                         getCartMultipleAddressListUseCase: GetCartMultipleAddressListUseCase,
                         userSessionInterface: UserSessionInterface): IMultipleAddressPresenter {
        return MultipleAddressPresenter(
                getCartMultipleAddressListUseCase,
                changeShippingAddressUseCase,
                userSessionInterface
        )
    }

}
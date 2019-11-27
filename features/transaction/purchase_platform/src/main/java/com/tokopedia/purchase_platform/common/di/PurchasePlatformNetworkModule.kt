package com.tokopedia.purchase_platform.common.di

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.authentication.AuthHelper.Companion.getUserAgent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.purchase_platform.common.data.api.CartApiInterceptor
import com.tokopedia.purchase_platform.common.data.api.CartResponseConverter
import com.tokopedia.purchase_platform.common.data.api.CommonPurchaseApiUrl
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

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@Module
class PurchasePlatformNetworkModule {

    @Provides
    fun provideFingerprintInterceptor(@ApplicationContext context: Context, userSessionInterface: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(context as NetworkRouter, userSessionInterface)
    }

    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @Provides
    fun getCartApiInterceptor(@ApplicationContext context: Context, userSessionInterface: UserSessionInterface): CartApiInterceptor {
        return CartApiInterceptor(context, context as NetworkRouter, userSessionInterface, CommonPurchaseApiUrl.HMAC_KEY)
    }

    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
                CommonPurchaseApiUrl.NET_READ_TIMEOUT,
                CommonPurchaseApiUrl.NET_WRITE_TIMEOUT,
                CommonPurchaseApiUrl.NET_CONNECT_TIMEOUT,
                CommonPurchaseApiUrl.NET_RETRY
        )
    }

    @Provides
    @PurchasePlatformQualifier
    fun provideCartApiOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                   cartApiInterceptor: CartApiInterceptor,
                                   okHttpRetryPolicy: OkHttpRetryPolicy,
                                   fingerprintInterceptor: FingerprintInterceptor,
                                   chuckInterceptor: ChuckInterceptor): OkHttpClient {

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
    fun provideAkamaiRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @PurchasePlatformAkamaiQualifier
    fun provideCartAkamaiApiOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                   cartApiInterceptor: CartApiInterceptor,
                                   okHttpRetryPolicy: OkHttpRetryPolicy,
                                   fingerprintInterceptor: FingerprintInterceptor,
                                   chuckInterceptor: ChuckInterceptor,
                                   @PurchasePlatformAkamaiQualifier remoteConfig: RemoteConfig): OkHttpClient {

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
        if (remoteConfig.getBoolean("akamai", true)) {
            builder.addInterceptor(AkamaiBotInterceptor())
        }
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides
    @PurchasePlatformQualifier
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

}
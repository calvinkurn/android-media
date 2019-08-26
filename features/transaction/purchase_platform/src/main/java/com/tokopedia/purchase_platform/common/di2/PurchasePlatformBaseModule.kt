package com.tokopedia.purchase_platform.common.di2

import android.content.Context
import com.example.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.purchase_platform.common.data.common.api.CartApiInterceptor
import com.tokopedia.purchase_platform.common.data.common.api.CartResponseConverter
import com.tokopedia.purchase_platform.common.data.common.api.CommonPurchaseApiUrl
import com.tokopedia.purchase_platform.common.router.ICheckoutModuleRouter
import com.tokopedia.purchase_platform.features.cart.view.di.CartScope
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
 * Created by Irfan Khoirul on 2019-08-26.
 */

@Module
class PurchasePlatformBaseModule {

    @Provides
    @CartScope
    fun provideRouter(@ApplicationContext context: Context): ICheckoutModuleRouter {
        return context as ICheckoutModuleRouter
    }

    @Provides
    fun provideFingerprintInterceptor(@ApplicationContext context: Context, userSessionInterface: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(context as NetworkRouter, userSessionInterface)
    }

    @Provides
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @Provides
    fun getCartApiInterceptor(@ApplicationContext context: Context): CartApiInterceptor {
        return CartApiInterceptor(context, context as AbstractionRouter, CommonPurchaseApiUrl.HMAC_KEY)
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
    fun provideCartApiOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                                   cartApiInterceptor: CartApiInterceptor,
                                   okHttpRetryPolicy: OkHttpRetryPolicy,
                                   fingerprintInterceptor: FingerprintInterceptor,
                                   chuckInterceptor: ChuckInterceptor): OkHttpClient {

        val builder = OkHttpClient.Builder()
                .readTimeout(okHttpRetryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
                .writeTimeout(okHttpRetryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
                .connectTimeout(okHttpRetryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
                .addInterceptor(AkamaiBotInterceptor())
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(cartApiInterceptor)
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckInterceptor)
        }
        return builder.build()
    }

    @Provides
    fun provideCartApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(CommonPurchaseApiUrl.BASE_URL)
                .addConverterFactory(CartResponseConverter.create())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

}
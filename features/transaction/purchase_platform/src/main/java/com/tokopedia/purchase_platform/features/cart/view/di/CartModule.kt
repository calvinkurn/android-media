package com.tokopedia.purchase_platform.features.cart.view.di

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
import com.tokopedia.purchase_platform.features.cart.data.api.CartApi
import com.tokopedia.purchase_platform.features.cart.data.repository.CartRepository
import com.tokopedia.purchase_platform.features.cart.data.repository.ICartRepository
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Irfan Khoirul on 2019-08-23.
 */

@Module
class CartModule {

    @Provides
    @CartScope
    fun provideFingerprintInterceptor(@ApplicationContext context: Context, userSession: UserSession): FingerprintInterceptor {
        return FingerprintInterceptor(context as NetworkRouter, userSession)
    }

    @Provides
    @CartScope
    fun provideChuckInterceptor(@ApplicationContext context: Context): ChuckInterceptor {
        return ChuckInterceptor(context)
    }

    @Provides
    @CartScope
    fun getCartApiInterceptor(@ApplicationContext context: Context): CartApiInterceptor {
        return CartApiInterceptor(context, context as AbstractionRouter, CommonPurchaseApiUrl.HMAC_KEY)
    }

    @Provides
    @CartScope
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

    @Provides
    fun provideICartRepository(cartApi: CartApi): ICartRepository {
        return CartRepository(cartApi)
    }

}
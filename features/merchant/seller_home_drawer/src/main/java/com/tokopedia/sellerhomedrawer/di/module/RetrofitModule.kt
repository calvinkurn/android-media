package com.tokopedia.sellerhomedrawer.di.module

import android.content.Context
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter
import com.tokopedia.cacheapi.interceptor.CacheApiInterceptor
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.RiskAnalyticsInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import com.tokopedia.sellerhomedrawer.data.constant.SellerDrawerUrl
import com.tokopedia.sellerhomedrawer.di.SellerHomeDashboardScope
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@SellerHomeDashboardScope
@Module
class RetrofitModule {

    private val baseUrl = SellerDrawerUrl.User.URL_NOTIFICATION

    @SellerHomeDashboardScope
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context,
                        networkRouter: NetworkRouter,
                        userSession: UserSession): Retrofit {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setPrettyPrinting().serializeNulls().create()
        val tkpdOkHttpBuilder = TkpdOkHttpBuilder(context, OkHttpClient.Builder())
                .addInterceptor(TkpdAuthInterceptor(context, networkRouter, userSession))
                .addInterceptor(FingerprintInterceptor(networkRouter, userSession))
                .addInterceptor(CacheApiInterceptor(context))
                .addInterceptor(RiskAnalyticsInterceptor(context))
        return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(TokopediaWsV4ResponseConverter())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(tkpdOkHttpBuilder.build())
                .build()
    }

    @SellerHomeDashboardScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }
}
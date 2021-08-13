package com.tokopedia.shop.review.shop.data.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import com.tokopedia.user.session.UserSession
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class BaseReputationService {
    open fun createRetrofit(context: Context?, baseUrl: String?, networkRouter: NetworkRouter?, userSession: UserSession?): Retrofit {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create()
        val tkpdOkHttpBuilder = TkpdOkHttpBuilder(context, OkHttpClient.Builder())
        tkpdOkHttpBuilder.addInterceptor(TkpdAuthInterceptor(context, networkRouter, userSession))
        tkpdOkHttpBuilder.addInterceptor(FingerprintInterceptor(networkRouter, userSession))
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(TokopediaWsV4ResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(tkpdOkHttpBuilder.build()).build()
    }
}
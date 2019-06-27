package com.tokopedia.common.network.coroutines.datasource

import android.content.Context
import com.tokopedia.common.network.data.source.cloud.api.RestApi
import com.tokopedia.network.CoroutineCallAdapterFactory
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.converter.StringResponseConverter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

object RestUtil {

    fun getApiInterface(interceptors: List<Interceptor?>?, context: Context): RestApi {
        val userSession: UserSessionInterface = UserSession(context.applicationContext)
        val okkHttpBuilder = TkpdOkHttpBuilder(context, OkHttpClient.Builder())
        if (interceptors != null) {
            okkHttpBuilder.addInterceptor(FingerprintInterceptor(context.applicationContext as NetworkRouter, userSession))
            for (interceptor in interceptors) {
                if (interceptor == null) {
                    continue
                }

                okkHttpBuilder.addInterceptor(interceptor)
            }
        }

        return Retrofit.Builder()
                .baseUrl("https://tokopedia.com/")
                .addConverterFactory(StringResponseConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .client(okkHttpBuilder.build()).build().create(RestApi::class.java)
    }
}
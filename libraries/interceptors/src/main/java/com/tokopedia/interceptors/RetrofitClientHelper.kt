package com.tokopedia.interceptors

import android.content.Context
import com.tokopedia.akamai_bot_lib.interceptor.GqlAkamaiBotInterceptor
import com.tokopedia.graphql.data.source.cloud.api.GraphqlUrl
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.TkpdOkHttpBuilder
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientHelper {

    fun getRetrofit(
        context: Context,
        userSession: UserSessionInterface,
        networkRouter: NetworkRouter
    ): Retrofit {
        val tkpdOkHttpBuilder = TkpdOkHttpBuilder(context, OkHttpClient.Builder())

        return Retrofit.Builder()
            .baseUrl(GraphqlUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                tkpdOkHttpBuilder
                    .addInterceptor(TkpdAuthInterceptor(context, networkRouter, userSession))
                    .addInterceptor(FingerprintInterceptor(networkRouter, userSession))
                    .addInterceptor(GqlAkamaiBotInterceptor())
                    .build()
            ).build()
    }
}
package com.tokopedia.talk.producttalk.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.DebugInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.talk.producttalk.domain.mapper.ProductTalkListMapper
import com.tokopedia.talk.producttalk.domain.usecase.GetProductTalkUseCase
import com.tokopedia.talk.producttalk.presenter.ProductTalkPresenter
import com.tokopedia.talk.producttalk.view.data.ProductTalkApi
import com.tokopedia.user.session.UserSession
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by nisie on 6/8/18.
 */
class ProductTalkDependencyInjector {

    object Companion {

        fun inject(context: Context): ProductTalkPresenter {

            val session = UserSession(context)

            val gson: Gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .setPrettyPrinting()
                    .serializeNulls().create()

            val stringResponseConverter = com.tokopedia.network.converter.StringResponseConverter()

            val retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
                    .addConverterFactory(stringResponseConverter)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

            val chuckInterceptor = ChuckInterceptor(context)

            val httpLoggingInterceptor = HttpLoggingInterceptor()

            if (GlobalConfig.isAllowDebuggingTools()) {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }

            val networkRouter : NetworkRouter = context as NetworkRouter

            val fingerprintInterceptor = com.tokopedia.network.interceptor.FingerprintInterceptor(networkRouter, session)

            val tkpdAuthInterceptor = com.tokopedia.network.interceptor.TkpdAuthInterceptor(context,
                    networkRouter, session)

            val builder: OkHttpClient.Builder = OkHttpClient.Builder()

            if (GlobalConfig.isAllowDebuggingTools()) {
                builder.addInterceptor(chuckInterceptor)
                builder.addInterceptor(DebugInterceptor())
                builder.addInterceptor(httpLoggingInterceptor)
            }

            val headerResponseInterceptor =
                    HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java)

            builder.addInterceptor(fingerprintInterceptor)
            builder.addInterceptor(tkpdAuthInterceptor)
            builder.addInterceptor(headerResponseInterceptor)

            val okHttpClient: OkHttpClient = builder.build()

            val retrofit: Retrofit = retrofitBuilder.baseUrl("https://ws.tokopedia.com/")
                    .client(okHttpClient)
                    .build()

            val productTalkApi: ProductTalkApi = retrofit.create(ProductTalkApi::class.java)

            val getProductTalkListMapper = ProductTalkListMapper()

            val getProductTalkUseCase = GetProductTalkUseCase(productTalkApi, getProductTalkListMapper)

            return ProductTalkPresenter(session, getProductTalkUseCase)
        }
    }
}
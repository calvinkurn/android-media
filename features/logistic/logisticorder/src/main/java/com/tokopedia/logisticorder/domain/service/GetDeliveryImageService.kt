package com.tokopedia.logisticorder.domain.service

import com.tokopedia.logisticorder.utils.TrackingPageUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GetDeliveryImageService {

    private var retrofit: Retrofit? = null
    val BASE_URL_API = "https://tokopedia.atlassian.net/rest/api/3/"
    val BASE_URL_JIRA = "https://apps.tokopedia.net"

    fun getClient(): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(TrackingPageUrl.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build()
        }
        return retrofit
    }

}
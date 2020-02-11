package com.tokopedia.tokofix.domain

import com.google.gson.GsonBuilder
import com.tokopedia.tokofix.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Author errysuprayogi on 09,February,2020
 */
object RetrofitClient {
    val webservice by lazy {
        Retrofit.Builder()
                .baseUrl(Config.PATCH_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(PatchApiService::class.java)
    }
}
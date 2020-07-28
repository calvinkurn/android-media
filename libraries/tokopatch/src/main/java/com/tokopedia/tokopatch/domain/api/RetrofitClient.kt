package com.tokopedia.tokopatch.domain.api

import com.google.gson.GsonBuilder
import com.tokopedia.tokopatch.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Author errysuprayogi on 09,February,2020
 */
object RetrofitClient {
    val webservice: PatchApiService by lazy {
        Retrofit.Builder()
                .baseUrl(Config.PATCH_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(PatchApiService::class.java)
    }
}
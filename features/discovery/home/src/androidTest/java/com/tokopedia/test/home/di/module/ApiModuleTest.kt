package com.tokopedia.home.di.module

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter
import com.tokopedia.core.gcm.network.GeneratedHostConverter
import com.tokopedia.home.common.ApiModule
import com.tokopedia.network.converter.StringResponseConverter
import dagger.Module
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class ApiModuleTest : ApiModule(){
    override fun provideHomeGraphQlRetrofit(okHttpClient: OkHttpClient?): Retrofit {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GeneratedHostConverter())
                .addConverterFactory(TokopediaWsV4ResponseConverter())
                .addConverterFactory(StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
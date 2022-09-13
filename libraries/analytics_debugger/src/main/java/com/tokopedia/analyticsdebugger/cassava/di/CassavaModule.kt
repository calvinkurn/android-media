package com.tokopedia.analyticsdebugger.cassava.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.analyticsdebugger.cassava.data.CassavaDatabase
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDao
import com.tokopedia.url.TokopediaUrl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author by furqan on 07/04/2021
 */
@Module
class CassavaModule() {

    @Singleton
    @Provides
    fun provideCassavaApi(context: Context): CassavaApi {
        val client = OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(context))
                .build()
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create()))
                .baseUrl(TokopediaUrl.getInstance().API)
                .client(client)
                .build()
        return retrofit.create(CassavaApi::class.java)
    }

    @Provides
    fun provideGtmDao(context: Context): GtmLogDao {
        return CassavaDatabase.getInstance(context).cassavaDao()
    }

}
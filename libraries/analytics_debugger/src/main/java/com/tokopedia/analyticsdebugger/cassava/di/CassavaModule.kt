package com.tokopedia.analyticsdebugger.cassava.di

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.analyticsdebugger.cassava.data.api.CassavaApi
import com.tokopedia.config.GlobalConfig
import com.tokopedia.url.TokopediaUrl
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author by furqan on 07/04/2021
 */
@Module
class CassavaModule {

    @CassavaScope
    @Provides
    fun provideApplication(@ApplicationContext context: Context): Application =
            context as Application

    @CassavaScope
    @Provides
    @CassavaQualifier
    fun provideChuckInterceptor(@ApplicationContext context: Context): Interceptor =
            ChuckerInterceptor(context)

    @CassavaScope
    @Provides
    fun provideOkHttpClient(@ApplicationScope httpLoggingInterceptor: HttpLoggingInterceptor,
                            @CassavaQualifier chuckerInterceptor: Interceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(chuckerInterceptor)
        }
        return builder.build()
    }

    @CassavaScope
    @Provides
    @CassavaQualifier
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                            .setPrettyPrinting()
                            .serializeNulls()
                            .create()))
                    .baseUrl(TokopediaUrl.getInstance().API)
                    .client(okHttpClient)
                    .build()

    @CassavaScope
    @Provides
    fun provideCassavaApi(retrofit: Retrofit): CassavaApi =
            retrofit.create(CassavaApi::class.java)

    companion object {
    }

}
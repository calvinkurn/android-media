package com.tokopedia.privacycenter.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.privacycenter.dsar.DsarHelper
import com.tokopedia.privacycenter.dsar.domain.GetCredentialsApi
import com.tokopedia.privacycenter.dsar.domain.OneTrustApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DsarModule {

    @Provides
    @ActivityScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
            context, GlobalConfig.isAllowDebuggingTools())
        return ChuckerInterceptor(
            context, collector)
    }

    @Provides
    @ActivityScope
    fun provideOneTrustOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckerInterceptor)
        }
        return builder.build()
    }

    @Provides
    @ActivityScope
    fun provideOneTrustRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            // move to tokopedia url when api ready
            .baseUrl("https://uat-de.onetrust.com/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @ActivityScope
    fun provideOneTrustApi(retrofit: Retrofit): OneTrustApi {
        return retrofit.create(OneTrustApi::class.java)
    }

    @Provides
    @ActivityScope
    fun provideDsarHelper(@ApplicationContext context: Context): DsarHelper {
        return DsarHelper(context)
    }

    @Provides
    @ActivityScope
    fun provideGetCredentialsApi(oneTrustApi: OneTrustApi, dsarHelper: DsarHelper): GetCredentialsApi {
        return GetCredentialsApi(oneTrustApi, dsarHelper)
    }

}

package com.tokopedia.privacycenter.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.privacycenter.remote.OneTrustApi
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class PrivacyCenterModule {

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideInterceptors(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor
    ): MutableList<Interceptor> {
        return if (GlobalConfig.isAllowDebuggingTools()) {
            val chucker = ChuckerInterceptor(context)
            mutableListOf(loggingInterceptor, chucker)
        } else {
            mutableListOf()
        }
    }

    @Provides
    @ActivityScope
    open fun provideRestRepository(
        interceptors: MutableList<Interceptor>,
        @ApplicationContext context: Context
    ): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @Provides
    @ActivityScope
    fun provideOneTrustOkHttpClient(interceptors: MutableList<Interceptor>): OkHttpClient {
        val builder = OkHttpClient.Builder()
        interceptors.forEach {
            builder.addInterceptor(it)
        }
        return builder.build()
    }

    @Provides
    @ActivityScope
    fun provideOneTrustRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TokopediaUrl.getInstance().ONE_TRUST)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @ActivityScope
    fun provideOneTrustApi(retrofit: Retrofit): OneTrustApi {
        return retrofit.create(OneTrustApi::class.java)
    }
}

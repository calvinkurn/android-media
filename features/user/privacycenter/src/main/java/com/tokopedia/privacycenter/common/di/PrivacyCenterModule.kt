package com.tokopedia.privacycenter.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.privacycenter.dsar.domain.OneTrustApi
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
class PrivacyCenterModule {

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideInterceptors(
        @ApplicationContext context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
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
    fun provideRestRepository(
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


}

package com.tokopedia.logout.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.logout.data.LogoutApi
import com.tokopedia.logout.data.LogoutUrl.Companion.BASE_URL
import com.tokopedia.logout.domain.mapper.LogoutMapper
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit


@LogoutScope
@Module
class LogoutModule {

    @LogoutScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @LogoutScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession = UserSession(context)

    @LogoutScope
    @Provides
    fun provideLogoutMapper(): LogoutMapper = LogoutMapper()

    @LogoutScope
    @Provides
    fun provideFingerprintInterceptor(@ApplicationContext context: Context, userSession: UserSession): FingerprintInterceptor {
        return FingerprintInterceptor(context as NetworkRouter, userSession)
    }

    @LogoutScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, userSession: UserSession): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context.applicationContext as NetworkRouter, userSession)
    }

    @LogoutScope
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            authInterceptor: TkpdAuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(fingerprintInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }

    @LogoutScope
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, retrofitBuilder: Retrofit.Builder): Retrofit {
        return retrofitBuilder.baseUrl(BASE_URL).client(okHttpClient).build()
    }

    @LogoutScope
    @Provides
    fun provideLogoutApi(retrofit: Retrofit): LogoutApi {
        return retrofit.create(LogoutApi::class.java)
    }
}
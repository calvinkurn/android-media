package com.tokopedia.loginregister.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.data.model.response.TkpdV4ResponseError
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.network.exception.HeaderErrorListResponse
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.loginregister.common.data.LoginRegisterUrl
import com.tokopedia.network.interceptor.DebugInterceptor
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreferenceManager
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.network.TkpdOldAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * @author by nisie on 10/15/18.
 */
@Module
class LoginRegisterModule {
    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideFingerprint(@ApplicationContext context: Context): FingerprintPreference {
        return FingerprintPreferenceManager(context)
    }

    @ActivityScope
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        tkpdAuthInterceptor: TkpdOldAuthInterceptor,
        chuckInterceptor: ChuckerInterceptor,
        debugInterceptor: DebugInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        fingerprintInterceptor: FingerprintInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(fingerprintInterceptor)
        builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val newRequest = chain.request().newBuilder()
            newRequest.addHeader("User-Agent", SessionModule.getUserAgent())
            chain.proceed(newRequest.build())
        })
        builder.addInterceptor(HeaderErrorResponseInterceptor(HeaderErrorListResponse::class.java))
        builder.addInterceptor(ErrorResponseInterceptor(TkpdV4ResponseError::class.java))
        builder.addInterceptor(AkamaiBotInterceptor(context))
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(chuckInterceptor)
            builder.addInterceptor(debugInterceptor)
            builder.addInterceptor(httpLoggingInterceptor)
        }
        return builder.build()
    }

    @ActivityScope
    @Provides
    fun provideLoginRegisterRetrofit(
        retrofitBuilder: Retrofit.Builder,
        @ActivityScope okHttpClient: OkHttpClient
    ): Retrofit {
        return retrofitBuilder.baseUrl(LoginRegisterUrl.BASE_DOMAIN)
            .client(okHttpClient)
            .build()
    }

    @ActivityScope
    @Provides
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @ActivityScope
    @Provides
    fun provideIrisSession(@ApplicationContext context: Context): IrisSession {
        return IrisSession(context)
    }
}

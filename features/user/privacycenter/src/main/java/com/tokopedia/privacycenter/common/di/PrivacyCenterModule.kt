package com.tokopedia.privacycenter.common.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.privacycenter.dsar.network.OneTrustInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
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
    fun provideOneTrustOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(OneTrustInterceptor())
        return builder.build()
    }

    @Provides
    @ActivityScope
    fun provideOneTrustRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://uat-de.onetrust.com/api")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
    }
}

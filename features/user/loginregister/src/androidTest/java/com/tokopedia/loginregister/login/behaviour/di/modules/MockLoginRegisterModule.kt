package com.tokopedia.loginregister.login.behaviour.di.modules

import com.tokopedia.loginregister.common.di.LoginRegisterModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MockLoginRegisterModule: LoginRegisterModule() {
    override fun provideLoginRegisterRetrofit(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder.baseUrl("http://127.0.0.1:8080")
                .client(okHttpClient)
                .build()
    }
}
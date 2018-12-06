package com.tokopedia.instantloan.di.module

import android.content.Context

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.network.interceptor.HeaderErrorResponseInterceptor
import com.tokopedia.instantloan.di.scope.InstantLoanScope
import com.tokopedia.instantloan.domain.interactor.GetBannersUserCase
import com.tokopedia.instantloan.domain.interactor.GetLoanProfileStatusUseCase
import com.tokopedia.instantloan.domain.interactor.PostPhoneDataUseCase
import com.tokopedia.instantloan.network.InstantLoanAuthInterceptor
import com.tokopedia.user.session.UserSession

import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import com.tokopedia.instantloan.network.InstantLoanUrl.BaseUrl.WEB_DOMAIN

@InstantLoanScope
@Module
class InstantLoanModule {

    @Provides
    fun provideGetLoanProfileStatusUseCase(instantLoanAuthInterceptor: InstantLoanAuthInterceptor, @ApplicationContext context: Context): GetLoanProfileStatusUseCase {
        return GetLoanProfileStatusUseCase(instantLoanAuthInterceptor, context)
    }

    @Provides
    fun provideGetBannersUseCase(instantLoanAuthInterceptor: InstantLoanAuthInterceptor, @ApplicationContext context: Context): GetBannersUserCase {
        return GetBannersUserCase(instantLoanAuthInterceptor, context)
    }

    @Provides
    fun providePostPhoneDataUseCase(instantLoanAuthInterceptor: InstantLoanAuthInterceptor, @ApplicationContext context: Context): PostPhoneDataUseCase {
        return PostPhoneDataUseCase(instantLoanAuthInterceptor, context)
    }

    @InstantLoanScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }
}


package com.tokopedia.deals.pdp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
class DealsPDPModule {

    @Provides
    @DealsPDPScope
    internal fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @DealsPDPScope
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @DealsPDPScope
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    @DealsPDPScope
    fun provideAuthInterceptors(
        @ApplicationContext context: Context,
        userSession: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
    }

    @Provides
    @DealsPDPScope
    fun provideInterceptors(
        tkpdAuthInterceptor: TkpdAuthInterceptor,
        fingerprintInterceptor: FingerprintInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): MutableList<Interceptor> {
        val listInterceptor = mutableListOf<Interceptor>()
        listInterceptor.add(fingerprintInterceptor)
        listInterceptor.add(tkpdAuthInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            listInterceptor.add(httpLoggingInterceptor)
        }

        return listInterceptor
    }

    @Provides
    @DealsPDPScope
    fun provideRestRepository(
        interceptors: MutableList<Interceptor>,
        @ApplicationContext context: Context
    ): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }
}

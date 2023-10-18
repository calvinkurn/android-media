package com.tokopedia.purchase_platform.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@Module
class PurchasePlatformNetworkModule {

    val NET_READ_TIMEOUT = 60
    val NET_WRITE_TIMEOUT = 60
    val NET_CONNECT_TIMEOUT = 60
    val NET_RETRY = 0

    @Provides
    fun provideFingerprintInterceptor(@ApplicationContext context: Context, userSessionInterface: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(context as NetworkRouter, userSessionInterface)
    }

    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
            NET_READ_TIMEOUT,
            NET_WRITE_TIMEOUT,
            NET_CONNECT_TIMEOUT,
            NET_RETRY
        )
    }
}

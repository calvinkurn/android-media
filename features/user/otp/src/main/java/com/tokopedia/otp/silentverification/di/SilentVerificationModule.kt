package com.tokopedia.otp.silentverification.di

import android.content.Context
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.otp.silentverification.domain.repository.GetEvUrlRepository
import com.tokopedia.otp.silentverification.domain.repository.GetEvUrlRepositoryImpl
import com.tokopedia.otp.silentverification.helper.NetworkClientHelper
import com.tokopedia.otp.silentverification.helper.NetworkClientHelperImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by Yoris on 28/10/21.
 */

@Module
class SilentVerificationModule(private val context: Context) {

    @Provides
    @SilentVerificationContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    @ActivityScope
    fun provideUserSession(
        @SilentVerificationContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideNetworkClientHelper(cellularNetworkRequest: NetworkRequest): NetworkClientHelper {
        return NetworkClientHelperImpl(cellularNetworkRequest)
    }

    @Provides
    @ActivityScope
    fun provideCellularNetworkRequest(): NetworkRequest {
        return NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
    }

    @Provides
    @ActivityScope
    fun provideGetEvUrlRepository(builder: OkHttpClient.Builder): GetEvUrlRepository {
        return GetEvUrlRepositoryImpl(builder)
    }

    @Provides
    @ActivityScope
    fun provideCustomOkHttpBuilder(chuckerInterceptor: ChuckerInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .writeTimeout(NetworkClientHelperImpl.SILENT_VERIF_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(NetworkClientHelperImpl.SILENT_VERIF_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(chuckerInterceptor)
    }

    @Provides
    @ActivityScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        val collector = ChuckerCollector(
                context, GlobalConfig.isAllowDebuggingTools())
        return ChuckerInterceptor(
                context, collector)
    }
}
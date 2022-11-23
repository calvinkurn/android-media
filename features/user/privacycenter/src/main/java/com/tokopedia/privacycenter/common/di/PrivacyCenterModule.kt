package com.tokopedia.privacycenter.common.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.config.GlobalConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
class PrivacyCenterModule {

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    @ActivityScope
    fun provideInterceptors(
        loggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): MutableList<Interceptor> {
        return mutableListOf<Interceptor>(
            loggingInterceptor
        ).also {
            if (GlobalConfig.isAllowDebuggingTools()) {
                it.add(chuckerInterceptor)
            }
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
}

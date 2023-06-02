package com.tokopedia.loginHelper.di.module

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.loginHelper.di.scope.LoginHelperScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
class LoginHelperModule {

    @LoginHelperScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @LoginHelperScope
    @Provides
    fun provideRestRepository(
        interceptors: MutableList<Interceptor>,
        @ApplicationContext context: Context
    ): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @LoginHelperScope
    @Provides
    fun provideInterceptors(loggingInterceptor: HttpLoggingInterceptor): MutableList<Interceptor> {
        return mutableListOf(loggingInterceptor)
    }

    @LoginHelperScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @LoginHelperScope
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }
}

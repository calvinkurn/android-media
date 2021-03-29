package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Provides
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import dagger.Module

@Module
class FingerprintInterceptorModule {
    @SearchScope
    @Provides
    fun provideFingerprintInterceptor(@ApplicationContext context: Context?, userSession: UserSessionInterface?): FingerprintInterceptor {
        return FingerprintInterceptor(context as NetworkRouter?, userSession)
    }
}
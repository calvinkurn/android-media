package com.tokopedia.search.result.network.interceptor

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.search.di.scope.SearchScope
import dagger.Provides
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.network.NetworkRouter
import dagger.Module

@Module
class TopAdsAuthInterceptorModule {
    @SearchScope
    @Provides
    fun provideTopAdsAuthInterceptor(@ApplicationContext context: Context?, userSessionInterface: UserSessionInterface?): TopAdsAuthInterceptor {
        return TopAdsAuthInterceptor(context, context as NetworkRouter?, userSessionInterface)
    }
}
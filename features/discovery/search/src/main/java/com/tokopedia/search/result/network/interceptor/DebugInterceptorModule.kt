package com.tokopedia.search.result.network.interceptor

import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class DebugInterceptorModule {
    @SearchScope
    @Provides
    fun provideDebugInterceptor(): DebugInterceptor {
        return DebugInterceptor()
    }
}
package com.tokopedia.search.di.module

import com.tokopedia.network.interceptor.TkpdBaseInterceptor
import com.tokopedia.search.di.scope.SearchScope
import dagger.Module
import dagger.Provides

@Module
class TkpdBaseInterceptorModule {
    @SearchScope
    @Provides
    fun provideTkpdBaseInterceptor(): TkpdBaseInterceptor {
        return TkpdBaseInterceptor()
    }
}
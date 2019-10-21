package com.tokopedia.search.di.module;

import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.network.interceptor.TkpdBaseInterceptor;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class TkpdBaseInterceptorModule {

    @SearchScope
    @Provides
    TkpdBaseInterceptor provideTkpdBaseInterceptor() {
        return new TkpdBaseInterceptor();
    }
}

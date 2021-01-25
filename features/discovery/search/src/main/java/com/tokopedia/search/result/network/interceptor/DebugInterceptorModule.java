package com.tokopedia.search.result.network.interceptor;

import com.tokopedia.search.di.scope.SearchScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DebugInterceptorModule {

    @SearchScope
    @Provides
    DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }
}

package com.tokopedia.search.result.network.interceptor;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class DebugInterceptorModule {

    @SearchScope
    @Provides
    DebugInterceptor provideDebugInterceptor() {
        return new DebugInterceptor();
    }
}

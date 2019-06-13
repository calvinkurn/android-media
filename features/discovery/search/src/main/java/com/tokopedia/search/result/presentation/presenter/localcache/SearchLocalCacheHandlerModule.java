package com.tokopedia.search.result.presentation.presenter.localcache;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchLocalCacheHandlerModule {

    @SearchScope
    @Provides
    SearchLocalCacheHandler provideSearchLocalCacheHandler() {
        return new SearchLocalCacheHandler();
    }
}

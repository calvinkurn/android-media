package com.tokopedia.search.result.presentation.presenter.localcache;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchLocalCacheHandlerModule {

    @SearchScope
    @Provides
    SearchLocalCacheHandler provideSearchLocalCacheHandler(@ApplicationContext Context context) {
        return new SearchLocalCacheHandler(context);
    }
}

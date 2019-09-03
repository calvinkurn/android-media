package com.tokopedia.search.result.presentation.view.activity;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.LocalCacheHandlerModule;
import com.tokopedia.search.di.module.RemoteConfigModule;
import com.tokopedia.search.di.module.SearchTrackingModule;
import com.tokopedia.search.di.module.UserSessionModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        UserSessionModule.class,
        SearchTrackingModule.class,
        RemoteConfigModule.class,
        LocalCacheHandlerModule.class
}, dependencies = BaseAppComponent.class)
public interface SearchViewComponent {

    void inject(SearchActivity searchActivity);
}
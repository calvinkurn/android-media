package com.tokopedia.search.result.presentation.view.activity;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.LocalCacheHandlerModule;
import com.tokopedia.search.di.module.SearchTrackingModule;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.result.shop.presentation.viewmodel.SearchShopViewModelFactoryModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        SearchShopViewModelFactoryModule.class,
        UserSessionModule.class,
        LocalCacheHandlerModule.class,
        SearchTrackingModule.class
}, dependencies = BaseAppComponent.class)
public interface SearchViewComponent {

    void inject(SearchActivity searchActivity);
}
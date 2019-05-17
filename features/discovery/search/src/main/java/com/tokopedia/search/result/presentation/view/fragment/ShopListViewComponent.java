package com.tokopedia.search.result.presentation.view.fragment;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.analytics.SearchTracking;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.result.presentation.presenter.shop.ShopListPresenterModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        ShopListPresenterModule.class,
        UserSessionModule.class,
        SearchTracking.class
}, dependencies = BaseAppComponent.class)
public interface ShopListViewComponent {

    void inject(ShopListFragment view);
}

package com.tokopedia.search.result.presentation.view.fragment;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.SearchTrackingModule;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.result.presentation.presenter.product.ProductListPresenterModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        ProductListPresenterModule.class,
        UserSessionModule.class,
        SearchTrackingModule.class
}, dependencies = BaseAppComponent.class)
public interface ProductListViewComponent {

    void inject(ProductListFragment view);
}

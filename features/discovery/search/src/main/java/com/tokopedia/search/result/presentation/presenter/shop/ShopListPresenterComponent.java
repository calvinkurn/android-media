package com.tokopedia.search.result.presentation.presenter.shop;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterUseCaseModule;
import com.tokopedia.search.result.domain.usecase.searchshop.SearchShopUseCaseModule;
import com.tokopedia.search.result.presentation.mapper.ShopViewModelMapperModule;
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandlerModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        GetDynamicFilterUseCaseModule.class,
        SearchLocalCacheHandlerModule.class,
        SearchShopUseCaseModule.class,
        ShopViewModelMapperModule.class,
        UserSessionModule.class
}, dependencies = BaseAppComponent.class)
public interface ShopListPresenterComponent {

    void inject(ShopListPresenter shopListPresenter);
}
package com.tokopedia.search.result.presentation.presenter.catalog;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterUseCaseModule;
import com.tokopedia.search.result.domain.usecase.searchcatalog.SearchCatalogUseCaseModule;
import com.tokopedia.search.result.presentation.mapper.CatalogViewModelMapperModule;
import com.tokopedia.search.result.presentation.presenter.localcache.SearchLocalCacheHandlerModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        GetDynamicFilterUseCaseModule.class,
        SearchLocalCacheHandlerModule.class,
        SearchCatalogUseCaseModule.class,
        CatalogViewModelMapperModule.class,
        UserSessionModule.class
}, dependencies = BaseAppComponent.class)
public interface CatalogListPresenterComponent {

    void inject(CatalogListPresenter catalogListPresenter);
}

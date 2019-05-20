package com.tokopedia.search.result.presentation.presenter.catalog;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.di.module.UserSessionModule;
import com.tokopedia.search.result.domain.usecase.getdynamicfilter.GetDynamicFilterUseCaseModule;
import com.tokopedia.search.result.domain.usecase.searchcatalog.SearchCatalogUseCaseModule;

import dagger.Component;

@SearchScope
@Component(modules = {
        GetDynamicFilterUseCaseModule.class,
        SearchCatalogUseCaseModule.class,
        UserSessionModule.class
})
public interface CatalogListPresenterComponent {

    void inject(CatalogListPresenter catalogListPresenter);
}

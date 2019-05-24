package com.tokopedia.search.result.presentation.presenter.catalog;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.presentation.CatalogListSectionContract;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class CatalogListPresenterModule {

    @SearchScope
    @Provides
    CatalogListSectionContract.Presenter provideCatalogListPresenter() {
        return new CatalogListPresenter();
    }
}

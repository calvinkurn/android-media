package com.tokopedia.search.result.presentation.presenter.product;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.presentation.ProductListSectionContract;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class ProductListPresenterModule {

    @SearchScope
    @Provides
    ProductListSectionContract.Presenter provideProductListSectionPresenter() {
        return new ProductListPresenter();
    }
}

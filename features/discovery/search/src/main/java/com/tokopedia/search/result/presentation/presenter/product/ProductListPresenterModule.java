package com.tokopedia.search.result.presentation.presenter.product;

import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.search.result.presentation.ProductListSectionContract;

import dagger.Module;
import dagger.Provides;

@Module
public class ProductListPresenterModule {

    @SearchScope
    @Provides
    ProductListSectionContract.Presenter provideProductListPresenter(ProductListPresenter presenter) {
        return presenter;
    }
}

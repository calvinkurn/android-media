package com.tokopedia.search.result.presentation.presenter.shop;

import com.tokopedia.search.di.scope.SearchScope;
import com.tokopedia.search.result.presentation.ShopListSectionContract;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class ShopListPresenterModule {

    @SearchScope
    @Provides
    ShopListSectionContract.Presenter provideShopListPresenter() {
        return new ShopListPresenter();
    }
}

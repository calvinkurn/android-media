package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.search.result.presentation.SearchContract;

import dagger.Module;
import dagger.Provides;

@SearchScope
@Module
public class SearchPresenterModule {

    @SearchScope
    @Provides
    SearchContract.Presenter getSearchPresenter() {
        return new SearchPresenter();
    }
}

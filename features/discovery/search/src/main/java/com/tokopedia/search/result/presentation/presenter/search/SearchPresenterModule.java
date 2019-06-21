package com.tokopedia.search.result.presentation.presenter.search;

import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.model.InitiateSearchModel;
import com.tokopedia.search.result.presentation.SearchContract;
import com.tokopedia.usecase.UseCase;

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

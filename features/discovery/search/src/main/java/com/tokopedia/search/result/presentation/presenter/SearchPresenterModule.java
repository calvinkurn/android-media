package com.tokopedia.search.result.presentation.presenter;

import com.tokopedia.search.result.presentation.SearchContract;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchPresenterModule {

    @Provides
    public SearchContract.Presenter getSearchPresenter() {
        return new SearchPresenter();
    }
}

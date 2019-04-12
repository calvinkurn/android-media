package com.tokopedia.search.presentation.presenter;

import com.tokopedia.search.presentation.SearchContract;

final class SearchPresenter implements SearchContract.Presenter {

    SearchContract.View view;

    @Override
    public void attachView(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}

package com.tokopedia.search.result.presentation.presenter.abstraction;

import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

public class TestSearchSectionPresenterSubclass
        extends SearchSectionPresenter<SearchSectionContract.View>
        implements SearchSectionContract.Presenter<SearchSectionContract.View> {

    @Override
    public void initInjector(SearchSectionContract.View view) {

    }

    @Override
    public void requestDynamicFilter(Map<String, Object> searchParameter) {
        getDynamicFilterUseCase.execute(RequestParams.create(), getDynamicFilterSubscriber(true));
    }
}

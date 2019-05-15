package com.tokopedia.search.result.presentation.presenter.abstraction;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

public abstract class SearchSectionPresenter<T extends SearchSectionContract.View>
        extends BaseDaggerPresenter<T>
        implements SearchSectionContract.Presenter<T> {

    protected void enrichWithAdditionalParams(RequestParams requestParams,
                                                       Map<String, String> additionalParams) {
        requestParams.putAllString(additionalParams);
    }

    protected RequestParams enrichWithFilterAndSortParams(RequestParams requestParams) {
        if (getView() == null) {
            return requestParams;
        }
        if (getView().getSelectedSort() != null) {
            requestParams.putAllString(getView().getSelectedSort());
        }
        if (getView().getSelectedFilter() != null) {
            requestParams.putAllString(getView().getSelectedFilter());
        }
        if (getView().getExtraFilter() != null) {
            requestParams.putAllString(getView().getExtraFilter());
        }
        return requestParams;
    }
}

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

    @Override
    public void requestDynamicFilter() {
        requestDynamicFilter(new HashMap<>());
    }

    @Override
    public void requestDynamicFilter(HashMap<String, String> additionalParams) {
        if (getView() == null) {
            return;
        }
        RequestParams params = getDynamicFilterParam();
        params = enrichWithFilterAndSortParams(params);
        params = enrichWithAdditionalParams(params, additionalParams);
        removeDefaultCategoryParam(params);
        getFilterFromNetwork(params);
    }

    protected RequestParams enrichWithAdditionalParams(RequestParams requestParams,
                                                       Map<String, String> additionalParams) {
        requestParams.putAllString(additionalParams);
        return requestParams;
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

    protected void removeDefaultCategoryParam(RequestParams params) {
        if (params.getString(SearchApiConst.SC, "").equals(SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SC)) {
            params.clearValue(SearchApiConst.SC);
        }
    }

    protected abstract RequestParams getDynamicFilterParam();
    protected abstract void getFilterFromNetwork(RequestParams requestParams);
}

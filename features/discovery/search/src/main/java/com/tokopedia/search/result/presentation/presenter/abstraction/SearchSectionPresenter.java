package com.tokopedia.search.result.presentation.presenter.abstraction;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.search.result.presentation.SearchSectionContract;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;
import com.tokopedia.usecase.RequestParams;

import java.util.Map;

public abstract class SearchSectionPresenter<T extends SearchSectionContract.View>
        extends BaseDaggerPresenter<T>
        implements SearchSectionContract.Presenter<T> {

    protected RequestDynamicFilterListener requestDynamicFilterListener;

    @Override
    public void setRequestDynamicFilterListener(RequestDynamicFilterListener requestDynamicFilterListener) {
        this.requestDynamicFilterListener = requestDynamicFilterListener;
    }

    protected void enrichWithAdditionalParams(RequestParams requestParams,
                                                       Map<String, String> additionalParams) {
        requestParams.putAllString(additionalParams);
    }
}

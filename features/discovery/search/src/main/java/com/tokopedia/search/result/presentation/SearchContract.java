package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchSubscriber;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;

public interface SearchContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {
        void onPause();
        void onResume();
        void onDestroy();

        void initiateSearch(SearchParameter searchParameter, boolean isForceSearch, InitiateSearchListener initiateSearchSubscriber);
    }
}
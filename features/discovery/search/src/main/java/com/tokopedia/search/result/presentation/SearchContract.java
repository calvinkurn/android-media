package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener;

import java.util.Map;

public interface SearchContract {
    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {
        void onPause();
        void onResume();
        void onDestroy();

        void initiateSearch(Map<String, Object> searchParameter, InitiateSearchListener initiateSearchSubscriber);
    }
}
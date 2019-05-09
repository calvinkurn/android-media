package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.newdiscovery.base.InitiateSearchListener;

import java.util.Map;

public interface SearchContract {
    interface View extends CustomerView {
        BaseAppComponent getBaseAppComponent();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initInjector(View view);
        void setInitiateSearchListener(InitiateSearchListener initiateSearchListener);

        void onPause();
        void onResume();
        void onDestroy();

        void initiateSearch(Map<String, Object> searchParameter, boolean isForceSearch);
    }
}
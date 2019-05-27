package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.search.result.presentation.view.listener.RequestDynamicFilterListener;

import java.util.HashMap;
import java.util.Map;

public interface SearchSectionContract {

    interface View extends CustomerView {
        HashMap<String, String> getSelectedSort();

        void setSelectedSort(HashMap<String, String> selectedSort);

        HashMap<String, String> getSelectedFilter();

        HashMap<String, String> getExtraFilter();

        void setSelectedFilter(HashMap<String, String> selectedFilter);

        void getDynamicFilter();

        void showRefreshLayout();

        void hideRefreshLayout();

        String getScreenNameId();

        void setTotalSearchResultCount(String formattedResultCount);

        BaseAppComponent getBaseAppComponent();

        void logDebug(String tag, String message);

        void launchLoginActivity(String shopId);
    }

    interface Presenter<T extends View> extends CustomerPresenter<T> {
        void initInjector(T view);

        void requestDynamicFilter(Map<String, Object> searchParameter);

        void setRequestDynamicFilterListener(RequestDynamicFilterListener requestDynamicFilterListener);
    }
}

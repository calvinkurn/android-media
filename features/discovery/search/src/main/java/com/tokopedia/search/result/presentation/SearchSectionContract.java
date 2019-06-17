package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.common.data.DynamicFilterModel;

import java.util.HashMap;
import java.util.Map;

public interface SearchSectionContract {

    interface View extends CustomerView {
        HashMap<String, String> getSelectedSort();

        void setSelectedSort(HashMap<String, String> selectedSort);

        HashMap<String, String> getSelectedFilter();

        void setSelectedFilter(HashMap<String, String> selectedFilter);

        void showRefreshLayout();

        void hideRefreshLayout();

        String getScreenNameId();

        void setTotalSearchResultCount(String formattedResultCount);

        BaseAppComponent getBaseAppComponent();

        void logDebug(String tag, String message);

        void renderDynamicFilter(DynamicFilterModel dynamicFilterModel);

        void renderFailRequestDynamicFilter();
    }

    interface Presenter<T extends View> extends CustomerPresenter<T> {
        void initInjector(T view);

        void requestDynamicFilter(Map<String, Object> searchParameter);
    }
}

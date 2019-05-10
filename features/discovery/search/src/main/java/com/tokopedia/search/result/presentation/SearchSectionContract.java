package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.discovery.common.data.DynamicFilterModel;

import java.util.HashMap;

public interface SearchSectionContract {

    interface View extends CustomerView {
        HashMap<String, String> getSelectedSort();

        void setSelectedSort(HashMap<String, String> selectedSort);

        HashMap<String, String> getSelectedFilter();

        HashMap<String, String> getExtraFilter();

        void setSelectedFilter(HashMap<String, String> selectedFilter);

        void getDynamicFilter();

        void renderDynamicFilter(DynamicFilterModel dynamicFilterModel);

        void renderFailGetDynamicFilter();

        void showRefreshLayout();

        void hideRefreshLayout();

        String getScreenNameId();

        void setTotalSearchResultCount(String formattedResultCount);
    }

    interface Presenter<T extends View> extends CustomerPresenter<T> {
        void initInjector(T view);

        void requestDynamicFilter();

        void requestDynamicFilter(HashMap<String, String> additionalParams);
    }
}

package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.usecase.RequestParams;

import java.util.List;
import java.util.Map;

public interface CatalogListSectionContract {

    interface View extends SearchSectionContract.View {

        String getQueryKey();

        String getDepartmentId();

        void setQueryKey(String queryKey);

        void setDepartmentId(String departmentId);

        void renderListView(List<Visitable> catalogViewModels);

        void renderNextListView(List<Visitable> catalogViewModels);

        void renderErrorView(String message);

        void renderRetryInit();

        void renderUnknown();

        void renderShareURL(String shareURL);

        void setHasNextPage(boolean hasNextPage);

        int getStartFrom();

        void initTopAdsParams(RequestParams requestParams);

        String getSource();

        void successRefreshCatalog(List<Visitable> visitables);

        void renderRetryRefresh();

        void setTopAdsEndlessListener();

        void unSetTopAdsEndlessListener();

        void backToTop();

        Map<String, Object> getSearchParameterMap();
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {

        void requestCatalogList();

        void requestCatalogLoadMore();

        void refreshSort();
    }
}

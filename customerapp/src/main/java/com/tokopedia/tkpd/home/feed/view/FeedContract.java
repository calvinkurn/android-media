package com.tokopedia.tkpd.home.feed.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * @author Kulomady on 12/15/16.
 */

interface FeedContract {

    interface View extends CustomerView {

        void showRetryGetAllDataFeed();

        void showFeedDataFromCache(List<RecyclerViewItem> dataFeed);

        void showRetryLoadMore();

        void showLoadMoreFeed(List<RecyclerViewItem> feed);

        boolean isLoading();

        void showRefreshLoading();

        void hideRefreshLoading();

        void enableLoadmore();

        void disableLoadmore();

        void refreshFeedData(List<RecyclerViewItem> data);

        void hideContentView();

        void showContentView();

        void showRefreshFailed();
    }

    interface Presenter extends CustomerPresenter<View> {

        void initializeDataFeed();

        void loadMoreDataFeed();

        void refreshDataFeed();

    }

}

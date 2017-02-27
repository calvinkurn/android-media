package com.tokopedia.tkpd.home.feed.view;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.domain.DefaultSubscriber;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.home.model.HistoryProductListItem;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetAllFeedDataPageUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetDataFeedCacheUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetFeedUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.LoadMoreFeedUseCase;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.domain.model.Feed;
import com.tokopedia.tkpd.home.feed.view.viewModel.EmptyFeedModel;
import com.tokopedia.tkpd.home.feed.view.viewModel.ProductFeedViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Kulomady on 12/15/16.
 */

public class FeedPresenter extends BaseDaggerPresenter<FeedContract.View>
        implements FeedContract.Presenter {

    private GetAllFeedDataPageUseCase feedDataPageUseCase;
    private GetDataFeedCacheUseCase feedDataCachePageUseCase;
    private LoadMoreFeedUseCase loadMoreFeedUseCase;
    private PagingHandler pagingHandler;


    @Inject
    public FeedPresenter(GetAllFeedDataPageUseCase feedDataPageUseCase,
                         GetDataFeedCacheUseCase feedDataCachePageUseCase,
                         LoadMoreFeedUseCase loadMoreFeedUseCase) {

        this.feedDataPageUseCase = feedDataPageUseCase;
        this.feedDataCachePageUseCase = feedDataCachePageUseCase;
        this.loadMoreFeedUseCase = loadMoreFeedUseCase;
        pagingHandler = new PagingHandler();
    }


    @Override
    public void detachView() {
        super.detachView();
        feedDataPageUseCase.unsubscribe();
        loadMoreFeedUseCase.unsubscribe();
    }

    @Override
    public void initializeDataFeed() {
        checkViewAttached();
        getView().hideContentView();
        feedDataCachePageUseCase.execute(RequestParams.EMPTY, new FeedCacheSubscriber());
    }

    @Override
    public void refreshDataFeed() {
        checkViewAttached();
        getView().showRefreshLoading();
        getView().disableLoadmore();
        feedDataPageUseCase.execute(RequestParams.EMPTY, new RefreshFeedSubcriber());
    }

    @Override
    public void loadMoreDataFeed() {
        if (pagingHandler.CheckNextPage()) {
            checkViewAttached();
            getView().hideRefreshLoading();
            pagingHandler.nextPage();
            loadMoreFeedUseCase.execute(getFeedRequestParams(), new LoadMoreFeedSubcriber(isPageOdd()));

        }

    }

    @NonNull
    private RequestParams getFeedRequestParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetFeedUseCase.KEY_ROWS, GetFeedUseCase.ROW_VALUE_DEFAULT);
        requestParams.putString(GetFeedUseCase.KEY_START, String.valueOf(getPagingIndex()));
        requestParams.putString((GetFeedUseCase.KEY_DEVICE), GetFeedUseCase.DEVICE_VALUE_DEFAULT);
        requestParams.putString(GetFeedUseCase.KEY_OB, GetFeedUseCase.OB_VALUE_DEFAULT);
        if (isPageOdd()) {
            requestParams.putBoolean(LoadMoreFeedUseCase.KEY_IS_INCLUDE_TOPADS, true);
            requestParams.putString(LoadMoreFeedUseCase.KEY_TOPADS_PAGE, getView().getTopAdsPage());
        } else {
            requestParams.putBoolean(LoadMoreFeedUseCase.KEY_IS_INCLUDE_TOPADS, false);
        }
        requestParams.putBoolean(GetFeedUseCase.KEY_IS_FIRST_PAGE, false);
        return requestParams;
    }

    private void setPagging(PagingHandler.PagingHandlerModel pagging) {
        pagingHandler.setHasNext(PagingHandler.CheckHasNext(pagging));
        pagingHandler.setPagingHandlerModel(pagging);
    }

    private void doCheckLoadMore() {
        if (pagingHandler.CheckNextPage()) {
            getView().enableLoadmore();
        } else {
            getView().disableLoadmore();
        }
    }

    private int getPagingIndex() {
        return pagingHandler != null ?
                pagingHandler.getPagingHandlerModel() != null
                        ? pagingHandler.getPagingHandlerModel().getStartIndex()
                        : 0
                : 0;
    }

    private boolean isPageOdd() {
        return pagingHandler.getPage() % 2 != 0;
    }

    private boolean isHasHistoryProduct(DataFeed dataFeed) {
        return dataFeed.getRecentProductList() != null
                && dataFeed.getRecentProductList().size() > 0;
    }

    private boolean isHasFeedProduct(DataFeed dataFeed) {
        Feed feed = dataFeed.getFeed();
        return (feed.isValid() && feed.getProducts() != null && feed.getProducts().size() > 0);
    }


    private class FeedCacheSubscriber extends DefaultSubscriber<DataFeed> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                getView().hideRefreshLoading();
                getView().hideContentView();
                getView().showEmptyHistoryProduct();
                getView().showEmptyFeed();
            }
            e.printStackTrace();
        }

        @Override
        public void onNext(DataFeed dataFeed) {
            if (isViewAttached()) {
                getView().hideRefreshLoading();
                ProductFeedViewModel productFeedViewModel = new ProductFeedViewModel(dataFeed);
                setPagging(productFeedViewModel.getPagingHandlerModel());
                if (productFeedViewModel.getData().size() > 0) {
                    validateDataFeed(dataFeed, productFeedViewModel);
                } else {
                    renderInvalidDataFeed(productFeedViewModel);
                }
                doCheckLoadMore();
            }
        }

        private void validateDataFeed(DataFeed dataFeed,
                                      ProductFeedViewModel productFeedViewModel) {

            if (!isHasHistoryProduct(dataFeed) && !isHasFeedProduct(dataFeed)) {
                renderInvalidDataFeed(productFeedViewModel);
            } else if (!isHasHistoryProduct(dataFeed) && isHasFeedProduct(dataFeed)) {
                getView().showContentView();
                final int recentViewPosition = 0;
                HistoryProductListItem fakeHistory
                        = new HistoryProductListItem(Collections.<ProductItem>emptyList());
                productFeedViewModel.getData().add(recentViewPosition, fakeHistory);
                displayFeed(productFeedViewModel);
                getView().hideEmptyFeed();
            } else if (isHasHistoryProduct(dataFeed) && !isHasFeedProduct(dataFeed)) {
                getView().hideEmptyHistoryProduct();
                getView().showContentView();
                getView().hideEmptyFeed();
                displayFeed(productFeedViewModel);
            } else {
                getView().showContentView();
                getView().hideEmptyHistoryProduct();
                getView().hideEmptyFeed();
                displayFeed(productFeedViewModel);

            }

        }

        private void renderInvalidDataFeed(ProductFeedViewModel productFeedViewModel) {
            getView().showContentView();
            getView().hideEmptyHistoryProduct();
            getView().hideEmptyFeed();

            productFeedViewModel.getData().add(
                    new HistoryProductListItem(Collections.<ProductItem>emptyList()));

            productFeedViewModel.getData().add(new EmptyFeedModel());
            displayFeed(productFeedViewModel);
        }

        private void displayFeed(ProductFeedViewModel productFeedViewModel) {
            getView().showFeedDataFromCache(productFeedViewModel.getData());
        }

    }

    private class LoadMoreFeedSubcriber extends DefaultSubscriber<DataFeed> {

        private boolean isIncludeTopAds;

        LoadMoreFeedSubcriber(boolean isIncludeTopAds) {

            this.isIncludeTopAds = isIncludeTopAds;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                getView().showRetryLoadMore();
            }
        }

        @Override
        public void onNext(DataFeed dataFeed) {
            if (isViewAttached()) {
                ProductFeedViewModel productFeedViewModel = new ProductFeedViewModel(dataFeed);
                setPagging(productFeedViewModel.getPagingHandlerModel());
                if (isIncludeTopAds) getView().increaseTopAdsPage();
                getView().showLoadMoreFeed(productFeedViewModel.getData());
                doCheckLoadMore();
            }
        }
    }

    private class RefreshFeedSubcriber extends DefaultSubscriber<DataFeed> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            getView().hideRefreshLoading();
            getView().showContentView();
            if (isViewAttached()) {
                getView().showRetryGetAllDataFeed();
            }
            e.printStackTrace();
        }

        @Override
        public void onNext(DataFeed dataFeed) {
            if (isViewAttached()) {
                getView().hideRefreshLoading();
                ProductFeedViewModel productFeedViewModel = new ProductFeedViewModel(dataFeed);
                if (productFeedViewModel.getData().size() > 0) {
                    setPagging(productFeedViewModel.getPagingHandlerModel());
                    validateDataFeed(dataFeed, productFeedViewModel);
                    doCheckLoadMore();
                } else {
                    if (!getView().isViewNotEmpty()) {
                        showEmptyView(productFeedViewModel);
                    }
                }
            }
        }


        private void validateDataFeed(DataFeed dataFeed,
                                      ProductFeedViewModel productFeedViewModel) {


            if (!isHasHistoryProduct(dataFeed) && !isHasFeedProduct(dataFeed)) {
                showEmptyView(productFeedViewModel);
            } else if (!isHasHistoryProduct(dataFeed) && isHasFeedProduct(dataFeed)) {
                getView().showContentView();
                RecyclerViewItem viewmodelHistory = getView().getViewmodelHistory();
                if (viewmodelHistory == null) {
                    final int recentViewPosition = 0;
                    HistoryProductListItem fakeHistory
                            = new HistoryProductListItem(Collections.<ProductItem>emptyList());
                    productFeedViewModel.getData().add(recentViewPosition, fakeHistory);
                } else {
                    productFeedViewModel.getData().add(0, viewmodelHistory);
                }
                displayRefreshData(productFeedViewModel);
                getView().hideEmptyFeed();
            } else if (isHasHistoryProduct(dataFeed) && !isHasFeedProduct(dataFeed)) {
                List<RecyclerViewItem> viewmodelFeed = getView().getViewmodelFeed();

                if (viewmodelFeed == null) {
                    productFeedViewModel.getData().add(new EmptyFeedModel());
                } else {
                    viewmodelFeed.set(0, productFeedViewModel.getData().get(0));
                    productFeedViewModel.setData(viewmodelFeed);
                }

                getView().hideEmptyHistoryProduct();
                getView().showContentView();
                getView().hideEmptyFeed();
                getView().showMessageRefreshFailed();
                displayRefreshData(productFeedViewModel);

            } else {
                getView().showContentView();
                getView().hideEmptyHistoryProduct();
                getView().hideEmptyFeed();
                displayRefreshData(productFeedViewModel);
            }


        }

        private void showEmptyView(ProductFeedViewModel productFeedViewModel) {
            if (!(getView().isViewNotEmpty())) {
                getView().showContentView();
                productFeedViewModel.getData().add(
                        new HistoryProductListItem(Collections.<ProductItem>emptyList()));

                productFeedViewModel.getData().add(new EmptyFeedModel());
                displayRefreshData(productFeedViewModel);
            }
        }

        private void displayRefreshData(ProductFeedViewModel productFeedViewModel) {
            getView().refreshFeedData(productFeedViewModel.getData());
        }

    }
}

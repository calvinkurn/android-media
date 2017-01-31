package com.tokopedia.tkpd.home.feed.view;

import com.tokopedia.core.base.DefaultSubscriber;
import com.tokopedia.core.base.presentation.BasePresenter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetAllFeedDataPageUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetDataFeedCacheUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.GetFeedUseCase;
import com.tokopedia.tkpd.home.feed.domain.interactor.LoadMoreFeedUseCase;
import com.tokopedia.tkpd.home.feed.domain.model.DataFeed;
import com.tokopedia.tkpd.home.feed.view.viewModel.ProductFeedViewModel;

import javax.inject.Inject;

/**
 * @author Kulomady on 12/15/16.
 */

public class FeedPresenter extends BasePresenter<FeedContract.View>
        implements FeedContract.Presenter {

    private GetAllFeedDataPageUseCase feedDataPageUseCase;
    private GetDataFeedCacheUseCase feedDataCachePageUseCase;
    private LoadMoreFeedUseCase loadMoreFeedUseCase;

    private PagingHandler pagingHandler;
    private GetFeedUseCase.RequestParams requestParams;

    @Inject
    public FeedPresenter(GetAllFeedDataPageUseCase feedDataPageUseCase,
                         GetDataFeedCacheUseCase feedDataCachePageUseCase,
                         LoadMoreFeedUseCase loadMoreFeedUseCase) {

        this.feedDataPageUseCase = feedDataPageUseCase;
        this.feedDataCachePageUseCase = feedDataCachePageUseCase;
        this.loadMoreFeedUseCase = loadMoreFeedUseCase;
        requestParams = new GetFeedUseCase
                .RequestParams(GetFeedUseCase.RequestParams.defaultParamsValue());

        pagingHandler = new PagingHandler();
    }



    @Override
    public void detachView() {
        super.detachView();
        feedDataPageUseCase.unsubcribe();
        loadMoreFeedUseCase.unsubscribe();
    }

    @Override
    public void initializeDataFeed() {
        checkViewAttached();
        getView().hideContentView();
        feedDataCachePageUseCase.execute(new FeedCacheSubscriber());
    }

    @Override
    public void refreshDataFeed() {
        checkViewAttached();
        getView().showRefreshLoading();
        getView().disableLoadmore();
        feedDataPageUseCase.execute(new RefreshFeedSubcriber());
    }

    @Override
    public void loadMoreDataFeed() {
        if (pagingHandler.CheckNextPage()) {
            checkViewAttached();
            pagingHandler.nextPage();
            requestParams.getValues()
                    .put(GetFeedUseCase.RequestParams.KEY_START, String.valueOf(getPagingIndex()));

            requestParams.setIncludeWithTopAds(isPageOdd());

            loadMoreFeedUseCase.execute(
                    requestParams,
                    new LoadMoreFeedSubcriber());
        }

    }



    private void setPagging(ProductFeedViewModel productFeedViewModel) {
        productFeedViewModel.getPagingHandlerModel();
        pagingHandler.setHasNext(
                PagingHandler.CheckHasNext(productFeedViewModel.getPagingHandlerModel()));

        pagingHandler.setPagingHandlerModel(productFeedViewModel.getPagingHandlerModel());
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

    private class FeedCacheSubscriber extends DefaultSubscriber<DataFeed> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                getView().hideContentView();
            }
            e.printStackTrace();
        }

        @Override
        public void onNext(DataFeed dataFeed) {
            if (isViewAttached()) {
                getView().hideRefreshLoading();
                ProductFeedViewModel productFeedViewModel = new ProductFeedViewModel(dataFeed);
                setPagging(productFeedViewModel);
                if (productFeedViewModel.getData().size() > 0) {
                    getView().showContentView();
                    getView().showFeedDataFromCache(productFeedViewModel.getData());
                } else {
                    getView().hideContentView();
                }
                doCheckLoadMore();
            }
        }

    }

    private class LoadMoreFeedSubcriber extends DefaultSubscriber<DataFeed> {
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
                setPagging(productFeedViewModel);
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
                getView().showContentView();
                ProductFeedViewModel productFeedViewModel = new ProductFeedViewModel(dataFeed);
                setPagging(productFeedViewModel);
                if (productFeedViewModel.getData().size() > 0) {
                    getView().refreshFeedData(productFeedViewModel.getData());
                } else {
                    getView().showRefreshFailed();
                }
                doCheckLoadMore();
            }
        }
    }
}

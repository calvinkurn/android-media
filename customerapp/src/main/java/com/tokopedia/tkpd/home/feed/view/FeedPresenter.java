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

    private GetAllFeedDataPageUseCase mFeedDataPageUseCase;
    private GetDataFeedCacheUseCase mFeedDataCachePageUseCase;
    private LoadMoreFeedUseCase mLoadMoreFeedUseCase;

    private PagingHandler mPagingHandler;
    private GetFeedUseCase.RequestParams mRequestParams;

    @Inject
    public FeedPresenter(GetAllFeedDataPageUseCase feedDataPageUseCase,
                         GetDataFeedCacheUseCase feedDataCachePageUseCase,
                         LoadMoreFeedUseCase loadMoreFeedUseCase) {

        mFeedDataPageUseCase = feedDataPageUseCase;
        mFeedDataCachePageUseCase = feedDataCachePageUseCase;
        mLoadMoreFeedUseCase = loadMoreFeedUseCase;
        mRequestParams = new GetFeedUseCase
                .RequestParams(GetFeedUseCase.RequestParams.defaultParamsValue());

        mPagingHandler = new PagingHandler();
    }



    @Override
    public void detachView() {
        super.detachView();
        mFeedDataPageUseCase.unsubcribe();
        mLoadMoreFeedUseCase.unsubscribe();
    }

    @Override
    public void initializeDataFeed() {
        checkViewAttached();
        getView().hideContentView();
        mFeedDataCachePageUseCase.execute(new FeedCacheSubscriber());
    }

    @Override
    public void refreshDataFeed() {
        checkViewAttached();
        getView().showRefreshLoading();
        getView().disableLoadmore();
        mFeedDataPageUseCase.execute(new RefreshFeedSubcriber());
    }

    @Override
    public void loadMoreDataFeed() {
        if (mPagingHandler.CheckNextPage()) {
            checkViewAttached();
            mPagingHandler.nextPage();
            mRequestParams.getValues()
                    .put(GetFeedUseCase.RequestParams.KEY_START, String.valueOf(getPagingIndex()));

            mRequestParams.setIncludeWithTopAds(isPageOdd());

            mLoadMoreFeedUseCase.execute(
                    mRequestParams,
                    new LoadMoreFeedSubcriber());
        }

    }



    private void setPagging(ProductFeedViewModel productFeedViewModel) {
        productFeedViewModel.getPagingHandlerModel();
        mPagingHandler.setHasNext(
                PagingHandler.CheckHasNext(productFeedViewModel.getPagingHandlerModel()));

        mPagingHandler.setPagingHandlerModel(productFeedViewModel.getPagingHandlerModel());
    }

    private void doCheckLoadMore() {
        if (mPagingHandler.CheckNextPage()) {
            getView().enableLoadmore();
        } else {
            getView().disableLoadmore();
        }
    }

    private int getPagingIndex() {
        return mPagingHandler != null ?
                mPagingHandler.getPagingHandlerModel() != null
                        ? mPagingHandler.getPagingHandlerModel().getStartIndex()
                        : 0
                : 0;
    }

    private boolean isPageOdd() {
        return mPagingHandler.getPage() % 2 != 0;
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

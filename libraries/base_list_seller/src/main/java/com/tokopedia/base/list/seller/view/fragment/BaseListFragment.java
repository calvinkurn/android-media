package com.tokopedia.base.list.seller.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;
import com.tokopedia.abstraction.common.utils.view.RefreshHandler;
import com.tokopedia.base.list.seller.R;
import com.tokopedia.base.list.seller.common.util.ItemType;
import com.tokopedia.base.list.seller.view.adapter.BaseListAdapter;
import com.tokopedia.base.list.seller.view.adapter.BaseRetryDataBinder;
import com.tokopedia.base.list.seller.view.listener.BaseListViewListener;
import com.tokopedia.base.list.seller.view.old.NoResultDataBinder;
import com.tokopedia.base.list.seller.view.old.RetryDataBinder;

import java.util.List;

/**
 * @author normansyahputa on 5/17/17.
 */
@Deprecated
public abstract class BaseListFragment<P, T extends ItemType> extends BasePresenterFragment<P> implements
        BaseListViewListener<T>, BaseListAdapter.Callback<T> {

    private static final int START_PAGE = 1;
    private static final int DEFAULT_TOTAL_ITEM = 0;

    protected BaseListAdapter<T> adapter;
    protected RecyclerView recyclerView;
    protected SwipeToRefresh swipeToRefresh;
    protected LinearLayoutManager layoutManager;
    protected int totalItem;
    protected int currentPage;
    protected SnackbarRetry snackBarRetry;

    private boolean searchMode;
    private ProgressDialog progressDialog;
    private RecyclerView.OnScrollListener onScrollListener;

    protected boolean isSearchMode() {
        return searchMode;
    }

    protected void setSearchMode(boolean searchMode) {
        this.searchMode = searchMode;
    }

    public BaseListFragment() {
        // Required empty public constructor
    }

    protected abstract BaseListAdapter<T> getNewAdapter();

    protected abstract void searchForPage(int page);

    protected NoResultDataBinder getEmptyViewDefaultBinder() {
        return new NoResultDataBinder(adapter);
    }

    protected NoResultDataBinder getEmptyViewNoResultBinder() {
        return getEmptyViewDefaultBinder();
    }

    public RetryDataBinder getRetryViewDataBinder(BaseListAdapter adapter) {
        return new BaseRetryDataBinder(adapter);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getActivity());
    }

    protected int getStartPage() {
        return START_PAGE;
    }

    protected boolean hasNextPage() {
        return adapter.getDataSize() < totalItem && totalItem != DEFAULT_TOTAL_ITEM;
    }

    protected int getCurrentPage() {
        return currentPage;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_base_list_seller;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeToRefresh = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    @Override
    protected void setViewListener() {
        super.setViewListener();
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItemPosition = layoutManager.findLastVisibleItemPosition();
                int visibleItem = layoutManager.getItemCount() - 1;
                if (lastItemPosition == visibleItem && hasNextPage()) {
                    setAndSearchForPage(currentPage + 1);
                    adapter.showRetryFull(false);
                    adapter.showLoading(true);
                }
            }
        };
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            recyclerView.addItemDecoration(itemDecoration);
        }
        recyclerView.addOnScrollListener(onScrollListener);
        if (swipeToRefresh != null) {
            new RefreshHandler(getActivity(), getView(), new RefreshHandler.OnRefreshHandlerListener() {
                @Override
                public void onRefresh(View view) {
                    onPullToRefresh();
                }
            });
        }
    }

    /**
     * Manual refresh from pull to refresh
     */
    protected void onPullToRefresh() {
        setAndSearchForPage(getStartPage());
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        currentPage = getStartPage();
        searchMode = false;
        adapter = getNewAdapter();
        adapter.setCallback(this);
        adapter.setEmptyView(getEmptyViewDefaultBinder());
        RetryDataBinder retryDataBinder = getRetryViewDataBinder(adapter);
        retryDataBinder.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                hideLoading();
                adapter.showRetryFull(false);
                adapter.showLoadingFull(true);
                resetPageAndSearch();
            }
        });
        adapter.setRetryView(retryDataBinder);
    }

    @Override
    protected void setActionVar() {
        super.setActionVar();
        loadData();
    }

    protected void loadData() {
        currentPage = getStartPage();
        adapter.clearData();
        adapter.showRetryFull(false);
        adapter.showLoadingFull(true);
        searchForPage(currentPage);
    }

    public void resetPageAndSearch() {
        currentPage = getStartPage();
        searchForPage(getStartPage());
    }

    protected void setAndSearchForPage(int page) {
        this.currentPage = page;
        searchForPage(page);
    }

    @Override
    public void onSearchLoaded(@NonNull List<T> list, int totalItem) {
        recyclerView.removeOnScrollListener(onScrollListener);
        recyclerView.addOnScrollListener(onScrollListener);
        this.totalItem = totalItem;
        hideLoading();
        if (totalItem <= 0 && getCurrentPage() == getStartPage()) {
            if (searchMode) {
                showViewSearchNoResult();
            } else {
                showViewEmptyList();
            }
        } else {
            showViewList(list);
        }
    }

    protected void showViewEmptyList() {
        adapter.setEmptyView(getEmptyViewDefaultBinder());
        adapter.clearData();
        layoutManager.scrollToPositionWithOffset(0, 0);
        adapter.showEmptyFull(true);
    }

    protected void showViewSearchNoResult() {
        adapter.setEmptyView(getEmptyViewNoResultBinder());
        adapter.clearData();
        layoutManager.scrollToPositionWithOffset(0, 0);
        adapter.showEmptyFull(true);
    }

    protected void showViewList(@NonNull List<T> list) {
        if (currentPage == getStartPage()) {
            adapter.clearData();
            layoutManager.scrollToPositionWithOffset(0, 0);
        }
        adapter.addData(list);
    }

    @Override
    public void onLoadSearchError(Throwable t) {
        hideLoading();
        if (adapter.getDataSize() > 0) {
            onLoadSearchErrorWithDataExist(t);
        } else {
            onLoadSearchErrorWithDataEmpty(t);
        }
    }

    /**
     * Error when adapter is empty
     * @param t
     */
    protected void onLoadSearchErrorWithDataEmpty(Throwable t) {
        recyclerView.removeOnScrollListener(onScrollListener);
        adapter.showRetryFull(true);
        if (swipeToRefresh != null) {
            swipeToRefresh.setEnabled(false);
        }
    }

    /**
     * Error when adapter is not empty
     * @param t
     */
    protected void onLoadSearchErrorWithDataExist(Throwable t) {
        showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                searchForPage(currentPage);
            }
        });
    }

    protected void hideLoading() {
        adapter.showLoading(false);
        adapter.showLoadingFull(false);
        adapter.showEmptyFull(false);
        adapter.showRetryFull(false);
        if (swipeToRefresh != null) {
            swipeToRefresh.setEnabled(true);
            swipeToRefresh.setRefreshing(false);
        }
        progressDialog.dismiss();
        hideSnackBarRetry();
    }

    private void showSnackBarRetry(NetworkErrorHelper.RetryClickedListener listener) {
        if (snackBarRetry == null) {
            initSnackbarRetry(listener);
            snackBarRetry.showRetrySnackbar();
            snackBarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        }
    }

    protected void initSnackbarRetry(NetworkErrorHelper.RetryClickedListener listener) {
        snackBarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), listener);
    }

    private void hideSnackBarRetry() {
        if (snackBarRetry != null) {
            snackBarRetry.hideRetrySnackbar();
            snackBarRetry = null;
        }
    }
}
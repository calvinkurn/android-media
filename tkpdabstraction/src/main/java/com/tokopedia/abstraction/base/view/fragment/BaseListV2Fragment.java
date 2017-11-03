package com.tokopedia.abstraction.base.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.BaseListV2Adapter;
import com.tokopedia.abstraction.base.view.adapter.type.ItemType;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.recyclerview.BaseListRecyclerView;
import com.tokopedia.abstraction.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.utils.snackbar.SnackbarRetry;

import java.util.List;

public abstract class BaseListV2Fragment<T extends ItemType> extends BaseDaggerFragment
        implements BaseListViewListener<T>{

    private BaseListV2Adapter<T> adapter;
    private BaseListRecyclerView recyclerView;
    private SwipeRefreshLayout swipeToRefresh;
    private SnackbarRetry snackBarRetry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = getNewAdapter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getRecyclerView(view);
        swipeToRefresh = getSwipeRefreshLayout(view);
        if (adapter!= null) {
            if (recyclerView!= null) {
                recyclerView.setAdapter(adapter);
            }
        }

        if (swipeToRefresh != null) {
            swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    hideSnackBarRetry();
                    swipeToRefresh.setRefreshing(true);
                    loadInitialData();
                }
            });
        }
    }

    protected abstract BaseListV2Adapter<T> getNewAdapter();

    public abstract BaseListRecyclerView getRecyclerView(View view);

    public abstract @Nullable SwipeRefreshLayout getSwipeRefreshLayout(View view);

    public void loadInitialData(){
        adapter.clearData();
        adapter.loadStartPage();
    }

    @Override
    public void onSearchLoaded(@NonNull List<T> list, int totalItem) {
        hideLoading();
        adapter.addData(list, totalItem);
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

    private void onLoadSearchErrorWithDataEmpty(Throwable t) {
        adapter.showRetry(true);
        if (swipeToRefresh != null) {
            swipeToRefresh.setEnabled(false);
        }
    }

    private void onLoadSearchErrorWithDataExist(Throwable t) {
        showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                adapter.loadNextPage();
            }
        });
    }

    protected void showLoading(){
        adapter.showLoading(true);
        hideSnackBarRetry();
    }

    protected void hideLoading() {
        adapter.showLoading(false);
        if (swipeToRefresh != null) {
            swipeToRefresh.setEnabled(true);
            swipeToRefresh.setRefreshing(false);
        }
        hideSnackBarRetry();
    }

    private void showSnackBarRetry(NetworkErrorHelper.RetryClickedListener listener) {
        if (snackBarRetry == null) {
            snackBarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), listener);
            snackBarRetry.setColorActionRetry(ContextCompat.getColor(getActivity(), R.color.green_400));
        }
        snackBarRetry.showRetrySnackbar();
    }

    private void hideSnackBarRetry() {
        if (snackBarRetry != null) {
            snackBarRetry.hideRetrySnackbar();
            snackBarRetry = null;
        }
    }
}
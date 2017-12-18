package com.tokopedia.abstraction.base.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.R;
import com.tokopedia.abstraction.base.view.adapter.BaseListAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.BaseListAdapterV2;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener2;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerviewListener;
import com.tokopedia.abstraction.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.utils.snackbar.SnackbarRetry;

import java.util.List;

public abstract class BaseListV2Fragment<T extends Visitable> extends BaseDaggerFragment
        implements BaseListViewListener2<T>, BaseListAdapterV2.OnAdapterInteractionListener<T> {

    private BaseListAdapterV2<T> adapter;
    private SwipeRefreshLayout swipeToRefresh;
    private SnackbarRetry snackBarRetry;
    private boolean isLoadMoreState;
    private boolean isAvailableLoadMore = true;
    private EndlessRecyclerviewListener endlessRecyclerviewListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new BaseListAdapterV2<>(getAdapterTypeFactory());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_list, container, false);
    }

    public RecyclerView getRecyclerView(View view) {
        return (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Nullable
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = getRecyclerView(view);
        LinearLayoutManager linearLayoutManager = getRecyclerViewLayoutManager();
        endlessRecyclerviewListener = new EndlessRecyclerviewListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (!isLoadMoreState && isAvailableLoadMore) {
                    isLoadMoreState = true;
                    onLoadMoreCalled(page, totalItemsCount);
                }
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerviewListener);
        swipeToRefresh = getSwipeRefreshLayout(view);
        if (adapter != null) {
            adapter.setOnAdapterInteractionListener(this);
            recyclerView.setAdapter(adapter);
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

        setInitialActionVar();
    }

    protected abstract void setInitialActionVar();

    protected void onLoadMoreCalled(int page, int totalItemsCount) {

    }

    protected void enableLoadMore() {
        isAvailableLoadMore = true;
    }

    protected void disableLoadMore() {
        isAvailableLoadMore = true;
    }

    protected LinearLayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
    }

    protected abstract BaseListAdapterTypeFactory getAdapterTypeFactory();

    protected void showLoading() {
        adapter.showLoading();
        hideSnackBarRetry();
    }

    public void loadInitialData() {
        adapter.clearData();
    }

    public final BaseListAdapterV2<T> getAdapter() {
        return adapter;
    }

    @Override
    public void renderList(@NonNull List<T> list) {
        adapter.clearData();
        if (list.size() == 0) {
            adapter.addElement(getEmptyDataViewModel());
        } else {
            adapter.addData(list);
        }
    }

    private Visitable getEmptyDataViewModel() {
        return new EmptyModel();
    }

    @Override
    public void renderAddList(@NonNull List<T> list) {
        adapter.hideLoading();
        adapter.addData(list);
    }

    @Override
    public void showGetListError() {
        adapter.hideLoading();
        if (adapter.getItemCount() > 0) {
            onGetListErrorWithExistingData();
        } else {
            onGetListErrorWithEmptyData();
        }
    }

    private void onGetListErrorWithEmptyData() {
        adapter.showErrorNetwork();
        if (swipeToRefresh != null) {
            swipeToRefresh.setEnabled(false);
        }
    }

    private void onGetListErrorWithExistingData() {
        showSnackBarRetry(new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {

            }
        });
    }

    protected void hideLoading() {
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

    @Override
    public void onItemClicked(T object) {

    }
}
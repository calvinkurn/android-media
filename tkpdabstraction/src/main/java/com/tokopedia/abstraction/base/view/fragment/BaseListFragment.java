package com.tokopedia.abstraction.base.view.fragment;

import android.content.Context;
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
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarRetry;

import java.util.List;

public abstract class BaseListFragment<T extends Visitable, F extends AdapterTypeFactory> extends BaseDaggerFragment
        implements BaseListViewListener<T>, BaseListAdapter.OnAdapterInteractionListener<T>,
        ErrorNetworkModel.OnRetryListener{

    private static final int DEFAULT_INITIAL_PAGE = 1;
    private BaseListAdapter<T, F> adapter;
    private SwipeRefreshLayout swipeToRefresh;
    private SnackbarRetry snackBarRetry;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private RecyclerView recyclerView;

    protected boolean isLoadingInitialData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = createAdapterInstance();
    }

    @NonNull
    protected BaseListAdapter<T, F> createAdapterInstance() {
        BaseListAdapter<T, F> baseListAdapter = new BaseListAdapter<>(getAdapterTypeFactory());
        baseListAdapter.setOnAdapterInteractionListener(this);
        return baseListAdapter;
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
        recyclerView = getRecyclerView(view);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            layoutManager = getRecyclerViewLayoutManager();
            if (layoutManager != null) {
                recyclerView.setLayoutManager(layoutManager);
            }
        }

        if (isLoadMoreEnabledByDefault()) {
            enableLoadMore();
        } else {
            disableLoadMore();
        }

        swipeToRefresh = getSwipeRefreshLayout(view);
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }

        if (swipeToRefresh != null) {
            swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onSwipeRefresh();
                }
            });
        }

        if (callInitialLoadAutomatically()) {
            showLoading();
            loadInitialData();
        }
    }

    public void onSwipeRefresh(){
        hideSnackBarRetry();
        swipeToRefresh.setRefreshing(true);
        loadInitialData();
    }

    protected void loadInitialData() {
        // Note that we don't clear data when load initial
        // instead, we just set the flag, so that the data is still there
        // do this flag check on renderList.
        isLoadingInitialData = true;
        loadData(getDefaultInitialPage());
    }

    /**
     * need for data with paging, page = 1 is initial load
     */
    public abstract void loadData(int page);

    public int getDefaultInitialPage() {
        return DEFAULT_INITIAL_PAGE;
    }

    protected boolean callInitialLoadAutomatically() {
        return true;
    }

    protected boolean isLoadMoreEnabledByDefault() {
        return true;
    }

    public void enableLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(recyclerView.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    showLoading();
                    loadData(page);
                }
            };
            endlessRecyclerViewScrollListener.setEndlessLayoutManagerListener(getEndlessLayoutManagerListener());
        }
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    @Nullable protected EndlessLayoutManagerListener getEndlessLayoutManagerListener(){
        return null;
    }

    public void disableLoadMore() {
        if (endlessRecyclerViewScrollListener != null) {
            recyclerView.removeOnScrollListener(endlessRecyclerViewScrollListener);
        }
    }

    protected RecyclerView.LayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
    }

    protected abstract F getAdapterTypeFactory();

    protected void showLoading() {
        adapter.removeErrorNetwork();
        adapter.showLoading();
        hideSnackBarRetry();
    }

    protected void showSwipeLoading(){
        adapter.removeErrorNetwork();
        hideSnackBarRetry();
        if (swipeToRefresh!=null) {
            swipeToRefresh.setRefreshing(true);
        }
    }

    public BaseListAdapter<T, F> getAdapter() {
        return adapter;
    }

    @Override
    public void renderList(@NonNull List<T> list, boolean hasNextPage) {
        hideLoading();
        // remove all unneeded element (empty/retry/loading/etc)
        if (isLoadingInitialData) {
            adapter.clearAllElements();
            if (endlessRecyclerViewScrollListener != null) {
                endlessRecyclerViewScrollListener.resetState();
            }
        } else {
            adapter.clearAllNonDataElement();
        }
        adapter.addElement(list);
        // update the load more state (paging/can loadmore)
        if (endlessRecyclerViewScrollListener != null) {
            endlessRecyclerViewScrollListener.updateStateAfterGetData();
            endlessRecyclerViewScrollListener.setHasNextPage(hasNextPage);
        }

        if (adapter.getItemCount() == 0) {
            // Note: add element should be the last in line.
            adapter.addElement(getEmptyDataViewModel());
        } else {
            //set flag to false, indicate that the initial data has been set.
            isLoadingInitialData = false;
        }
    }

    /**
     * default hasNextData for this function is false.
     */
    @Override
    public void renderList(@NonNull List<T> list) {
        renderList(list, false);
    }

    protected Visitable getEmptyDataViewModel() {
        return new EmptyModel();
    }

    @Override
    public void showGetListError(Throwable throwable) {
        hideLoading();

        // update the load more state (paging/can loadmore)
        if (endlessRecyclerViewScrollListener != null) {
            endlessRecyclerViewScrollListener.updateStateAfterGetData();
        }

        // Note: add element should be the last in line.
        if (adapter.getItemCount() > 0) {
            onGetListErrorWithExistingData(throwable);
        } else {
            onGetListErrorWithEmptyData(throwable);
        }
    }

    private void onGetListErrorWithEmptyData(Throwable throwable) {
        String message = getMessageFromThrowable(getView().getContext(), throwable);
        adapter.showErrorNetwork(message, this);
        if (swipeToRefresh != null) {
            swipeToRefresh.setEnabled(false);
        }
    }

    private void onGetListErrorWithExistingData(Throwable throwable) {
        showSnackBarRetry(throwable, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                showLoading();
                if (endlessRecyclerViewScrollListener != null) {
                    endlessRecyclerViewScrollListener.loadMoreNextPage();
                } else {
                    loadInitialData();
                }
            }
        });
    }

    @Override
    public void onRetryClicked() {
        showLoading();
        loadInitialData();
    }

    protected void hideLoading() {
        if (swipeToRefresh != null) {
            swipeToRefresh.setEnabled(true);
            swipeToRefresh.setRefreshing(false);
        }
        adapter.hideLoading();
        hideSnackBarRetry();
    }

    private void showSnackBarRetry(Throwable throwable, NetworkErrorHelper.RetryClickedListener listener) {
        if (snackBarRetry == null) {
            String message = getMessageFromThrowable(getView().getContext(), throwable);
            snackBarRetry = NetworkErrorHelper.createSnackbarWithAction(getActivity(), message, listener);
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

    protected String getMessageFromThrowable(Context context, Throwable t){
        return ErrorHandler.getErrorMessage(context, t);
    }

    protected int getCurrentPage(){
        return endlessRecyclerViewScrollListener.getCurrentPage();
    }

}
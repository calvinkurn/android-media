package com.tokopedia.abstraction.base.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;

import java.util.List;

public abstract class BaseListFragment<T extends Visitable, F extends AdapterTypeFactory> extends BaseDaggerFragment
        implements BaseListViewListener<T>, BaseListAdapter.OnAdapterInteractionListener<T>,
        ErrorNetworkModel.OnRetryListener{

    private static final int DEFAULT_INITIAL_PAGE = 1;
    private BaseListAdapter<T, F> adapter;
    private SwipeRefreshLayout swipeToRefresh;
    private Snackbar snackBarRetry;
    protected EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
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
        if (hasInitialSwipeRefresh()) {
            return inflater.inflate(R.layout.fragment_base_list_swipe, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_base_list, container, false);
        }
    }

    protected boolean hasInitialSwipeRefresh (){
        return false;
    }

    public RecyclerView getRecyclerView(View view) {
        return (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Nullable
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        if (hasInitialSwipeRefresh()) {
            return view.findViewById(R.id.swipe_refresh_layout);
        }
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
            loadInitialData();
        }
    }

    public void onSwipeRefresh(){
        hideSnackBarRetry();
        swipeToRefresh.setRefreshing(true);
        loadInitialData();
    }

    protected void loadInitialData() {
        // Load all from the beginning / reset data
        // Need to clear all data to avoid invalid data in case of error
        isLoadingInitialData = true;
        adapter.clearAllElements();
        showLoading();
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
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener();
            endlessRecyclerViewScrollListener.setEndlessLayoutManagerListener(getEndlessLayoutManagerListener());
        }
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    protected EndlessRecyclerViewScrollListener createEndlessRecyclerViewListener(){
        return new EndlessRecyclerViewScrollListener(recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                showLoading();
                loadData(page);
            }
        };
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
        adapter.setLoadingModel(getLoadingModel());
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
            clearAllData();
        } else {
            adapter.clearAllNonDataElement();
        }
        adapter.addElement(list);
        // update the load more state (paging/can loadmore)
        updateScrollListenerState(hasNextPage);

        if (isListEmpty()) {
            // Note: add element should be the last in line.
            adapter.addElement(getEmptyDataViewModel());
        } else {
            //set flag to false, indicate that the initial data has been set.
            isLoadingInitialData = false;
        }
    }

    public boolean isListEmpty(){
        return adapter.getItemCount() == 0;
    }

    public void updateScrollListenerState(boolean hasNextPage){
        if (endlessRecyclerViewScrollListener != null) {
            endlessRecyclerViewScrollListener.updateStateAfterGetData();
            endlessRecyclerViewScrollListener.setHasNextPage(hasNextPage);
        }
    }

    public void clearAllData(){
        adapter.clearAllElements();
        if (endlessRecyclerViewScrollListener != null) {
            endlessRecyclerViewScrollListener.resetState();
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
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setContent(getString(R.string.title_no_result));
        return emptyModel;
    }

    @Override
    public void showGetListError(Throwable throwable) {
        hideLoading();

        updateStateScrollListener();

        // Note: add element should be the last in line.
        if (adapter.getItemCount() > 0) {
            onGetListErrorWithExistingData(throwable);
        } else {
            onGetListErrorWithEmptyData(throwable);
        }
    }

    protected void updateStateScrollListener(){
        // update the load more state (paging/can loadmore)
        if (endlessRecyclerViewScrollListener != null) {
            endlessRecyclerViewScrollListener.updateStateAfterGetData();
        }
    }

    protected void onGetListErrorWithEmptyData(Throwable throwable) {
        if (getView() != null) {
            String message = getMessageFromThrowable(getView().getContext(), throwable);
            adapter.showErrorNetwork(message, this);
            if (swipeToRefresh != null) {
                swipeToRefresh.setEnabled(false);
            }
        }
    }

    protected void onGetListErrorWithExistingData(Throwable throwable) {
        showSnackBarRetry(throwable,v -> {
                if (endlessRecyclerViewScrollListener != null) {
                    endlessRecyclerViewScrollListener.loadMoreNextPage();
                } else {
                    loadInitialData();
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

    private void showSnackBarRetry(Throwable throwable, View.OnClickListener listener) {
        if (snackBarRetry == null) {
            String message = getMessageFromThrowable(getView().getContext(), throwable);

            snackBarRetry = ToasterError.make(getView(), message, BaseToaster.LENGTH_INDEFINITE)
                    .setAction(R.string.retry_label, listener);
        }
        snackBarRetry.show();
    }

    private void hideSnackBarRetry() {
        if (snackBarRetry != null) {
            snackBarRetry.dismiss();
            snackBarRetry = null;
        }
    }

    protected String getMessageFromThrowable(Context context, Throwable t){
        return ErrorHandler.getErrorMessage(context, t);
    }

    protected int getCurrentPage(){
        return endlessRecyclerViewScrollListener.getCurrentPage();
    }

    public LoadingModel getLoadingModel() {
        return new LoadingModel();
    }
}
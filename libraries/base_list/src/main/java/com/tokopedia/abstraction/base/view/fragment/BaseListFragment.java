package com.tokopedia.abstraction.base.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.listener.EndlessLayoutManagerListener;
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.baselist.R;
import com.tokopedia.unifycomponents.Toaster;

import java.util.List;

/**
 * Fragment that using recyclerview and has capability to show the data list,
 * handle simple pagination when onscroll to bottom, can have swiperefresh to refresh the list,
 * have capability to show loading, empty state, and error state
 *
 * To use, we have to extend the fragment
 * class Fragment extends BaseListFragment<{Model}, {AdapterFactory}>
 * {Model} are the data we want to show in the list. If there are many model type, we can extend the model.
 * Generally, there are several main functions you need call on the fragment.
 * 1. loadInitialData()
 *    function to refresh the data from start (page 1).
 *    As default this function will be called onViewCreated, onSwipeRefresh, and onRetry,
 *    so most of time, no need to call this anymore.
 * 2. loadData(page: Int)
 *    override this function in the fragment.
 *    Add the logic to retrieve the data for that page in this function.
 *    In first time, page will be 1.
 * 3. renderList(data, hasNextPage) to show the list. (generally this after onSuccess get Data)
 * 4. showGetListError(Throwable) to show the error. (generally this after onError get Data)
 *
 * HOW TO CUSTOMIZE
 * 1 change [[DEFAULT VIEW]]
 *   As default, the view for this fragment are vertical recyclerview.
 *   If we need to change the view, override onCreateView() function and supply the view we want.
 *    -> You also need to override getRecyclerView(View view) and getSwipeRefreshLayout(View view)
 *       if the view's is different with the default view's id.
 * 2.Add/remove [[SWIPE REFRESH]]
 *   override hasInitialSwipeRefresh() to true/false
 * 3.[[LOAD INITIAL DATA CALL POINT]]
 *   default will call loadInitialData() in onViewCreated
 *   override callInitialLoadAutomatically() to false, then call loadInitialData() in place you want
 * 4.enable/disable [[LOADMORE]]
 *   override isLoadMoreEnabledByDefault to true/false
 *   or
 *   call enableLoadMore or disableLoadMore programmatically.
 * 5.change recyclerview's [[LAYOUT_MANAGER]] to horizontal/vertical/grid
 *   override getRecyclerViewLayoutManager()
 * 6.Customize Error icon [[ERROR]] From Network
 *   override fun createAdapterInstance(): BaseListAdapter<{Model}, {AdapterFactory}> {
 *       val adapter =  super.createAdapterInstance()
 *       adapter.errorNetworkModel = ErrorNetworkModel().apply {
 *           iconDrawableRes = R.drawable.ic_error_cloud_green
 *       }
 *       return adapter
 *   }
 *   override getMessageFromThrowable(context, Throwable) to change the error message based on throwable
 *   or call
 *   super.showGetListError(MessageErrorException(getString(R.string.sorry_flash_sale_is_canceled)))
 * 7.Customize [[EMPTY STATE]]
 *   override getEmptyDataViewModel() and create new EmptyModel()
 *   To manually show fullEmptyPage in adapter: call adapter.clearAllElements() and showEmpty(). Most of time, no need to call it.
 * 8.Customize [[LOADING STATE]]
 *   override getLoadingModel()
 */
public abstract class BaseListFragment<T extends Visitable, F extends AdapterTypeFactory> extends BaseDaggerFragment
        implements BaseListViewListener<T>, BaseListAdapter.OnAdapterInteractionListener<T>,
        ErrorNetworkModel.OnRetryListener{

    private static final int DEFAULT_INITIAL_PAGE = 1;
    private static final int DEFAULT_MAX_ROW_FULL_PAGE = 10;

    private BaseListAdapter<T, F> adapter;
    protected SwipeRefreshLayout swipeToRefresh;
    protected EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private RecyclerView recyclerView;
    private Snackbar toasterSnackbar;

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

    @Nullable
    public RecyclerView getRecyclerView(View view) {
        if (view == null) return null;
        return (RecyclerView) view.findViewById(getRecyclerViewResourceId());
    }

    @Nullable
    public SwipeRefreshLayout getSwipeRefreshLayout(View view) {
        if (hasInitialSwipeRefresh()) {
            return view.findViewById(getSwipeRefreshLayoutResourceId());
        }
        return null;
    }

    public int getSwipeRefreshLayoutResourceId(){
        return R.id.swipe_refresh_layout;
    }

    public int getRecyclerViewResourceId(){
        return R.id.recycler_view;
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

    /***
     * Load all from the beginning (page 1)
     * Show Loading is automatically called.
     * Also clear all data bewfore loading to avoid invalid data in case of error
     */
    protected void loadInitialData() {
        isLoadingInitialData = true;
        adapter.clearAllElements();
        showLoading();
        loadData(getDefaultInitialPage());
    }

    /**
     * need for data with paging, page = 1 is initial load
     */
    public abstract void loadData(int page);

    /**
     * It is recommended to never override this value when we have paging (to avoid error when load More Data)
     */
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

    /**
     * Function to show the list.
     * Behavior is APPEND, means if there is 10 data already and we add another 10, will become 20,
     *   * except loadInitialData() will clear the data and reset page to 0.
     * @param list data to show for this page
     * @param hasNextPage true if we want recyclerview to load more data at the bottom of recyclerview.
     */
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
            showEmpty();
        } else {
            //set flag to false, indicate that the initial data has been set.
            isLoadingInitialData = false;
        }

        // load next page data if adapter data less than minimum scrollable data
        // when the list has next page and auto load next page is enabled
        if (adapter.getDataSize() < getMinimumScrollableNumOfItems() && isAutoLoadEnabled()
                && hasNextPage && endlessRecyclerViewScrollListener !=  null) {
            endlessRecyclerViewScrollListener.loadMoreNextPage();
        }
    }

    public boolean isListEmpty(){
        return adapter.getItemCount() == 0;
    }

    public void showEmpty(){
        // Note: add element should be the last in line.
        adapter.addElement(getEmptyDataViewModel());
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

    @Override
    public void renderList(@NonNull List<T> list) {
        renderList(list, false);
    }

    protected Visitable getEmptyDataViewModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setContent(getString(R.string.title_no_result));
        return emptyModel;
    }

    /**
     * show the default error
     * If the error happen at loadmore, snackbar will be shown
     * If error happens when data is empty, fullscreeen message will be shown instead.
     * @param throwable
     */
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
        if (getActivity() != null) {
            String message = getMessageFromThrowable(getActivity(), throwable);
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

    /**
     * return the minimum items to make page scrollable
     * this number will be used as threshold to automatically load
     * next page data if it have next page and have auto load enabled
     *
     * @return int
     */
    protected int getMinimumScrollableNumOfItems() {
        return DEFAULT_MAX_ROW_FULL_PAGE;
    }

    /**
     * auto load enabled is used to call load next page
     * automatically if list data less than minimum num of rows for full page
     * and the list still has next page
     *
     * @return boolean
     */
    protected boolean isAutoLoadEnabled() {
        return false;
    }

    private void showSnackBarRetry(Throwable throwable, View.OnClickListener listener) {
        if (getActivity() == null) {
            return;
        }
        String message = getMessageFromThrowable(getActivity(), throwable);
        if (getView() != null) {
            toasterSnackbar = Toaster.build(getView(), message, Snackbar.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                    getString(R.string.retry_label), listener);
            toasterSnackbar.show();
        }
    }

    protected void hideSnackBarRetry() {
        try{
            if (toasterSnackbar != null) {
                toasterSnackbar.dismiss();
            }
        }catch (Exception e){}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toasterSnackbar = null;
    }

    protected String getMessageFromThrowable(Context context, Throwable t){
        return ErrorHandler.getErrorMessage(context, t);
    }

    /**
     * Current page from the scroll listener.
     * Return 0 if there is no data or even still loading first page.
     * Will return 1 after rendering first page successfully
     */
    protected int getCurrentPage(){
        return endlessRecyclerViewScrollListener.getCurrentPage();
    }

    public LoadingModel getLoadingModel() {
        return new LoadingModel();
    }
}
package com.tokopedia.core.customadapter;

import com.tokopedia.core.util.DataBindAdapter;
import com.tokopedia.core.util.DataBinder;

/**
 * Created by Nisie on 1/22/16.
 */

/**
 * Use base adapter with visitor pattern from tkpd abstraction
 */
@Deprecated
public class BaseLinearRecyclerViewAdapter extends DataBindAdapter{

    public static final int VIEW_LOADING = 999;
    public static final int VIEW_RETRY = 888;
    public static final int VIEW_EMPTY = 777;
    protected int loading = 0;
    protected int retry = 0;
    protected int empty = 0;
    private LoadingDataBinder loadingView;
    private RetryDataBinder retryView;
    private NoResultDataBinder emptyView;
    private RetryDataBinder.OnRetryListener listener;

    public BaseLinearRecyclerViewAdapter() {
        loadingView = new LoadingDataBinder(this);
        emptyView = new NoResultDataBinder(this);
        retryView = new RetryDataBinder(this);
        retryView.setOnRetryListenerRV(onRetryClicked());
    }

    public void setLoadingView(LoadingDataBinder loadingView) {
        this.loadingView = loadingView;
    }

    public void setEmptyView(NoResultDataBinder emptyView) {
        this.emptyView = emptyView;
    }

    public void setRetryView(RetryDataBinder retryView) {
        this.retryView = retryView;
    }

    @Override
    public int getItemCount() {
        return empty + loading + retry;
    }

    @Override
    public int getItemViewType(int position) {
        if(isLoading()){
            return VIEW_LOADING;
        }else if(isRetry()) {
            return VIEW_RETRY;
        }else{
            return VIEW_EMPTY;
        }
    }

    @Override
    public <T extends DataBinder> T getDataBinder(int viewType) {

        switch (viewType){
            case VIEW_LOADING: return (T) loadingView;
            case VIEW_RETRY : return (T) retryView;
            case VIEW_EMPTY : return (T) emptyView;
            default: return (T) emptyView;
        }

    }

    @Override
    public int getBinderPosition(int position) {
        return position;
    }

    private RetryDataBinder.OnRetryListener onRetryClicked() {
        return new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                showRetry(false);
                showLoading(true);
                notifyDataSetChanged();
                listener.onRetryCliked();
            }
        };
    }

    public void showEmpty(boolean isEmpty) {
        if (isEmpty) {
            empty = 1;
        } else {
            empty = 0;
        }
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return empty == 1;
    }

    public void showRetry(boolean isRetry) {
        if (isRetry) {
            retry = 1;
        } else {
            retry = 0;
        }
        retryView.setIsFullScreen(false);
        notifyDataSetChanged();
    }

    public boolean isRetry() {
        return retry == 1;
    }

    public void showLoading(boolean isLoading) {
        if (isLoading) {
            loading = 1;
        } else {
            loading = 0;
        }
        loadingView.setIsFullScreen(false);
        notifyDataSetChanged();
    }

    public boolean isLoading() {
        return loading == 1;
    }

    public void setOnRetryListenerRV(RetryDataBinder.OnRetryListener listener) {
        this.listener = listener;
    }

    public void showEmptyFull(boolean isEmpty) {
        if (isEmpty) {
            retry = 0;
            loading = 0;
            empty = 1;
        } else {
            empty = 0;
        }
        emptyView.setIsFullScreen(isEmpty);
        notifyDataSetChanged();
    }

    public void showLoadingFull(boolean isLoading) {
        if (isLoading) {
            retry = 0;
            loading = 1;
            empty = 0;
        } else {
            loading = 0;
        }
        loadingView.setIsFullScreen(isLoading);
        notifyDataSetChanged();
    }

    public void showRetryFull(boolean isRetry) {
        if (isRetry) {
            retry = 1;
            loading = 0;
            empty = 0;
        } else {
            retry = 0;
        }
        retryView.setIsFullScreen(isRetry);
        notifyDataSetChanged();
    }
}
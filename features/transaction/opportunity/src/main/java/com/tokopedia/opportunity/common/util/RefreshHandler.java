package com.tokopedia.opportunity.common.util;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.opportunity.R;

public class RefreshHandler {

    private SwipeToRefresh swipeToRefreshLayout;
    private OnRefreshHandlerListener RefreshHandlerListener;
    private boolean isRefreshing = false;

    public interface OnRefreshHandlerListener {
        void onRefresh();
    }

    public RefreshHandler(View view, OnRefreshHandlerListener RefreshListener) {
        swipeToRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        this.RefreshHandlerListener = RefreshListener;
        swipeToRefreshLayout.setOnRefreshListener(onSwipeRefresh());
    }

    public void setView(View view) {
        swipeToRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeToRefreshLayout.setOnRefreshListener(onSwipeRefresh());
    }

    private SwipeRefreshLayout.OnRefreshListener onSwipeRefresh() {
        return this::startRefresh;
    }

    public void startRefresh() {
        isRefreshing = true;
        swipeToRefreshLayout.setRefreshing(true);
        RefreshHandlerListener.onRefresh();
    }

    public void finishRefresh() {
        isRefreshing = false;
        swipeToRefreshLayout.setRefreshing(false);
    }

    public void setRefreshing(boolean isRefreshing){
        this.isRefreshing = isRefreshing;
        swipeToRefreshLayout.setRefreshing(isRefreshing);
    }

    public void setIsRefreshing(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
    }


    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setPullEnabled(boolean enabled){
        swipeToRefreshLayout.setEnabled(enabled);
    }
}

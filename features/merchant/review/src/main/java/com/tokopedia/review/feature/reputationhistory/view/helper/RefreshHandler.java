package com.tokopedia.review.feature.reputationhistory.view.helper;

import android.view.View;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.review.R;

/**
 * modify by mnormansyah 18 feb 2017
 */
public class RefreshHandler {

	private SwipeToRefresh swipeToRefreshLayout;
	private OnRefreshHandlerListener RefreshHandlerListener;
	private boolean isRefreshing = false;
	private View view;

	public interface OnRefreshHandlerListener {
		void onRefresh(View view);
	}

	public RefreshHandler(SwipeToRefresh swipeToRefresh,
						  OnRefreshHandlerListener onRefreshHandlerListener){
		this.swipeToRefreshLayout = swipeToRefresh;
		this.RefreshHandlerListener = onRefreshHandlerListener;
		swipeToRefresh.setOnRefreshListener(onSwipeRefresh());
	}

	public void setView(View view) {
		swipeToRefreshLayout = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
		swipeToRefreshLayout.setOnRefreshListener(onSwipeRefresh());
	}

	private SwipeRefreshLayout.OnRefreshListener onSwipeRefresh() {
		return new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				startRefresh();
			}
		};
	}

	public void startRefresh() {
		isRefreshing = true;
		swipeToRefreshLayout.setRefreshing(true);
		RefreshHandlerListener.onRefresh(view);
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
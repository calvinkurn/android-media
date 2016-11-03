package com.tokopedia.tkpd.util;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.app.MainApplication;
import com.tokopedia.tkpd.customwidget.SwipeToRefresh;
import com.tokopedia.tkpd.var.NotificationVariable;

public class RefreshHandler {

	private SwipeToRefresh swipeToRefreshLayout;
	private OnRefreshHandlerListener RefreshHandlerListener;
	private boolean isRefreshing = false;
	private Activity context;
	private View view;
	private NotificationVariable notif;


	public interface OnRefreshHandlerListener {
		void onRefresh(View view);
	}

	public RefreshHandler(Activity context, View view, OnRefreshHandlerListener RefreshListener) {
		swipeToRefreshLayout = (SwipeToRefresh) view.findViewById(R.id.swipe_refresh_layout);
		notif = MainApplication.getNotifInstance();
		notif.setContext(context);
		this.RefreshHandlerListener = RefreshListener;
		swipeToRefreshLayout.setOnRefreshListener(onSwipeRefresh());
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
		notif.GetNotif();
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
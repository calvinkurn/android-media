package com.tokopedia.core.inboxreputation.listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.tokopedia.core.inboxreputation.adapter.InboxReputationAdapter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.RefreshHandler;

/**
 * Created by Nisie on 21/01/16.
 */
public interface InboxReputationFragmentView {

    RefreshHandler.OnRefreshHandlerListener onRefresh();

    boolean isAllReviewChecked();

    String getKeyword();

    void setActionEnabled(boolean isEnabled);

    boolean isRefreshing();

    RecyclerView.OnScrollListener onScrollListener();

    void showNoResult();

    InboxReputationAdapter getAdapter();

    void clearData();

    void setRefreshing(boolean isRefreshing);

    String getFilter();

    boolean getUserVisibleHint();

    Bundle getArguments();

    Activity getActivity();

    void startActivityForResult(Intent intent, int requestCode);

    void showRefreshing();

    void removeError();

    void finishLoading();

    void showEmptyState(NetworkErrorHelper.RetryClickedListener listener);

    void showEmptyState(String error, NetworkErrorHelper.RetryClickedListener listener);

    void showSnackbar(String error, NetworkErrorHelper.RetryClickedListener listener);

    void showSnackbar(NetworkErrorHelper.RetryClickedListener listener);
}

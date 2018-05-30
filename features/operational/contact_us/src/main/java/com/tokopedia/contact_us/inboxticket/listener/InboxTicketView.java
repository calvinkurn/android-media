package com.tokopedia.contact_us.inboxticket.listener;

import android.view.View;

import com.tokopedia.contact_us.inboxticket.adapter.InboxTicketAdapter;
import com.tokopedia.core.network.NetworkErrorHelper;

/**
 * Created by Nisie on 4/21/16.
 */
public interface InboxTicketView {

    void goToHelp();

    InboxTicketAdapter getAdapter();

    void finishLoading();

    void removeError();

    void setActionEnabled(boolean isEnabled);

    String getFilter();

    String getStatus();

    void showSnackbar(String error);

    void showSnackbar(String error,View.OnClickListener listener);

    void finishRefreshing();

    void showEmptyState(String error, NetworkErrorHelper.RetryClickedListener listener);

    void showRefreshLoading();

    void showLoadingBottom();
}

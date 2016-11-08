package com.tokopedia.core.inboxticket.presenter;

import android.view.View;

import com.tokopedia.core.util.RefreshHandler;

/**
 * Created by Nisie on 4/21/16.
 */
public interface InboxTicketFragmentPresenter {
    RefreshHandler.OnRefreshHandlerListener onRefresh();

    void getInboxTicket();

    void setCache();

    void loadMore();

    View.OnClickListener onRetry(boolean isRefresh);

    boolean isLoading();

    boolean hasNextPage();

    void onConfirmFilterClicked();

    void onDestroyView();
}

package com.tokopedia.core.inboxticket.listener;

import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.customadapter.ImageUpload;
import com.tokopedia.core.inboxticket.adapter.InboxTicketDetailAdapter;
import com.tokopedia.core.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.core.inboxticket.model.inboxticketdetail.TicketReplyDatum;
import com.tokopedia.core.network.NetworkErrorHelper;

/**
 * Created by Nisie on 4/26/16.
 */
public interface InboxTicketDetailFragmentView {
    void finishLoading();

    InboxTicketDetailAdapter getAdapter();

    void setRefreshing(boolean isRefreshing);

    void setReplyVisibility(int visibility);

    void onSuccessGetInboxTicketDetail(InboxTicketDetail result);

    void setActionsEnabled(boolean isEnabled);

    void showError(String error);

    void showRetry(String message, View.OnClickListener listener);

    void onSuccessSetRating(Bundle resultData);

    void onFailedSetRating(Bundle resultData);

    void showProgressDialog();

    String getComment();

    void showReplyDialog();

    void showEmptyState(String message, NetworkErrorHelper.RetryClickedListener listener);

    com.tokopedia.core.inboxticket.adapter.ImageUploadAdapter getImageAdapter();

    void addImage(ImageUpload image);

    void onSuccessReply(TicketReplyDatum ticketReply);

    void showRefreshing();

    void showLoadingAll();

    void setResultSuccess();

    void removeError();
}

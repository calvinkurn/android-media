package com.tokopedia.tkpd.inboxticket.listener;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.tkpd.customadapter.ImageUpload;
import com.tokopedia.tkpd.customadapter.ImageUploadAdapter;
import com.tokopedia.tkpd.inboxreputation.model.inboxreputation.InboxReputation;
import com.tokopedia.tkpd.inboxticket.adapter.InboxTicketDetailAdapter;
import com.tokopedia.tkpd.inboxticket.model.InboxTicketParam;
import com.tokopedia.tkpd.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.tkpd.inboxticket.model.inboxticketdetail.TicketReplyDatum;
import com.tokopedia.tkpd.network.NetworkErrorHelper;

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

    com.tokopedia.tkpd.inboxticket.adapter.ImageUploadAdapter getImageAdapter();

    void addImage(ImageUpload image);

    void onSuccessReply(TicketReplyDatum ticketReply);

    void showRefreshing();

    void showLoadingAll();

    void setResultSuccess();

    void removeError();
}

package com.tokopedia.tkpd.inboxticket.presenter;

import android.content.Intent;
import android.view.View;

import com.tokopedia.tkpd.inboxticket.model.inboxticketdetail.InboxTicketDetail;
import com.tokopedia.tkpd.inboxticket.model.inboxticketdetail.TicketReplyDatum;
import com.tokopedia.tkpd.util.RefreshHandler;

/**
 * Created by Nisie on 4/22/16.
 */
public interface InboxTicketDetailFragmentPresenter {
    void getViewMore();

    void onRefresh();

    void setCache();

    void onSendButtonClicked();

    void sendReply();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void actionImagePicker();

    void actionCamera();

    void onDestroyView();
}

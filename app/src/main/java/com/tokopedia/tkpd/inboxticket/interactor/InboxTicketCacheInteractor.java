package com.tokopedia.tkpd.inboxticket.interactor;

import com.tokopedia.tkpd.inboxticket.model.inboxticket.InboxTicket;
import com.tokopedia.tkpd.inboxticket.model.inboxticketdetail.InboxTicketDetail;

/**
 * Created by Nisie on 4/19/16.
 */
public interface InboxTicketCacheInteractor {

    void getInboxTicketCache(GetInboxTicketCacheListener listener);

    void setInboxTicketCache(InboxTicket result);

    void getInboxTicketDetailCache(String ticketId, GetInboxTicketDetailCacheListener listener);

    void setInboxTicketDetailCache(String ticketId, InboxTicketDetail result);

    void deleteCache();

    interface GetInboxTicketCacheListener {
        void onSuccess(InboxTicket result);

        void onError(Throwable e);
    }

    public interface GetInboxTicketDetailCacheListener {
        void onSuccess(InboxTicketDetail result);

        void onError(Throwable e);
    }
}

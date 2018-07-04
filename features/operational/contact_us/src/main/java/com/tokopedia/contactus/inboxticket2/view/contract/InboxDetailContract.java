package com.tokopedia.contactus.inboxticket2.view.contract;

import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;

/**
 * Created by pranaymohapatra on 22/06/18.
 */

public interface InboxDetailContract {
    interface InboxDetailView extends InboxBaseContract.InboxBaseView {
        void showCollapsedMessages();

        void hideMessages();

        void renderMessageList(Tickets ticketDetail);

        void toggleSearch(int visibility);
    }

    interface InboxDetailPresenter extends InboxBaseContract.InboxBasePresenter {
        CustomEditText.Listener getSearchListener();

    }
}

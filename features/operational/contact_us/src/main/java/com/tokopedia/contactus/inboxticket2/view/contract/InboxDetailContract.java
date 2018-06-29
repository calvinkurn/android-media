package com.tokopedia.contactus.inboxticket2.view.contract;

/**
 * Created by pranaymohapatra on 22/06/18.
 */

public interface InboxDetailContract {
    interface InboxDetailView extends InboxBaseContract.InboxBaseView {
        void showCollapsedMessages();

        void hideMessages();

        void renderMessageList();
    }

    interface InboxDetailPresenter extends InboxBaseContract.InboxBasePresenter{

    }
}

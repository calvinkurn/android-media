package com.tokopedia.contactus.inboxticket2.view.contract;

import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.contactus.inboxticket2.domain.TicketsItem;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;

import java.util.List;

/**
 * Created by pranaymohapatra on 18/06/18.
 */

public interface InboxListContract {

    interface InboxListView extends InboxBaseContract.InboxBaseView {
        void renderTicketList(List<TicketsItem> ticketList);

        void hideFilter();

        void showFilter();

        void toggleSearch(int visibility);

        void toggleEmptyLayout(int visibility);

        void removeFooter();

        void addFooter();

        LinearLayoutManager getLayoutManager();

        void scrollRv();
    }

    interface InboxListPresenter extends InboxBaseContract.InboxBasePresenter {
        void onClickFilter();

        void setFilter(int position);

        void onClickTicket(int index);

        void deleteTicket();

        void scrollList();

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

        CustomEditText.Listener getSearchListener();
    }
}

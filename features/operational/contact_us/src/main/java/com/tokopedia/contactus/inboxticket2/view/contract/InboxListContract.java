package com.tokopedia.contactus.inboxticket2.view.contract;

import android.support.v7.widget.LinearLayoutManager;

import com.tokopedia.contactus.inboxticket2.domain.TicketsItem;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;

import java.util.List;

public interface InboxListContract {

    interface InboxListView extends InboxBaseContract.InboxBaseView {
        void renderTicketList(List<TicketsItem> ticketList);

        void hideFilter();

        void showFilter();

        void toggleEmptyLayout(int visibility);

        void removeFooter();

        void addFooter();

        LinearLayoutManager getLayoutManager();

        void scrollRv();
    }

    interface InboxListPresenter extends InboxBaseContract.InboxBasePresenter {
        void onClickFilter();

        void setFilter(int position);

        void onClickTicket(int index, boolean isOfficialStore);

        void scrollList();

        void onRecyclerViewScrolled(LinearLayoutManager layoutManager);

        CustomEditText.Listener getSearchListener();
    }
}

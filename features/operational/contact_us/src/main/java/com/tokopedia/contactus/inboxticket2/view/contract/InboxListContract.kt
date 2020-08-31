package com.tokopedia.contactus.inboxticket2.view.contract

import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText
import com.tokopedia.usecase.RequestParams

interface InboxListContract {
    interface InboxListView : InboxBaseView {
        fun renderTicketList(ticketItemList: MutableList<InboxTicketListResponse.Ticket.Data.TicketItem>)
        fun hideFilter()
        fun showFilter()
        fun toggleEmptyLayout(visibility: Int)
        fun toggleNoTicketLayout(visibility: Int, name: String)
        fun removeFooter()
        fun addFooter()
        fun getLayoutManager(): LinearLayoutManager
        fun scrollRv()
    }

    interface InboxListPresenter : InboxBasePresenter {
        fun onClickFilter()
        fun setFilter(position: Int)
        fun onClickTicket(index: Int, isOfficialStore: Boolean)
        fun scrollList()
        fun onRecyclerViewScrolled(layoutManager: LinearLayoutManager)
        fun getSearchListener(): CustomEditText.Listener?
        fun getTicketList(requestParams: RequestParams?)
    }
}
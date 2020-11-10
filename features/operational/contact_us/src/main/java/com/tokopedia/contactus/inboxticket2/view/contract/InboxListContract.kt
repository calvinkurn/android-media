package com.tokopedia.contactus.inboxticket2.view.contract

import android.text.Spanned
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.contactus.inboxticket2.data.model.ChipTopBotStatusResponse
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBasePresenter
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract.InboxBaseView
import com.tokopedia.contactus.inboxticket2.view.customview.ChatWidgetToolTip
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
        fun showChatBotWidget()
        fun hideChatBotWidget()
    }

    interface Presenter : InboxBasePresenter {
        fun onClickFilter()
        fun setFilter(position: Int)
        fun onClickTicket(index: Int, isOfficialStore: Boolean)
        fun scrollList()
        fun onRecyclerViewScrolled(layoutManager: LinearLayoutManager)
        fun getSearchListener(): CustomEditText.Listener?
        fun getTicketList(requestParams: RequestParams?)
        fun getTopBotStatus()
        fun getChatbotApplink(): String
        fun getWelcomeMessage(): Spanned
        fun getNotifiactionIndiactor(): Boolean
    }
}
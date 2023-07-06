package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.chatbot2.data.inboxTicketList.InboxTicketListResponse

sealed class TicketListState {
    object ShowContactUs : TicketListState()
    data class BottomSheetData(val noticeData: InboxTicketListResponse.Ticket.Data.NoticeItem) :
        TicketListState()
}

package com.tokopedia.chatbot.view.viewmodel

import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse

sealed class TicketListState {
    object ShowContactUs : TicketListState()
    data class BottomSheetData(val noticeData: InboxTicketListResponse.Ticket.Data.NoticeItem) :
        TicketListState()
}

package com.tokopedia.contactus.inboxtickets.view.inbox.uimodel

import com.tokopedia.contactus.inboxtickets.data.model.InboxTicketListResponse

data class InboxState(
    val idMessage: String = "",
    val welcomeMessage: String = "",
    val showChatBotWidget: Boolean = false,
    val unReadNotification: Boolean = false,
    val offset: Int = 0,
    val ticketItems: List<InboxTicketListResponse.Ticket.Data.TicketItem> = arrayListOf(),
    val isFilteredData: Boolean = false
)

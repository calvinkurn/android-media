package com.tokopedia.contactus.inboxtickets.view.inbox.uimodel

import com.tokopedia.contactus.inboxtickets.data.model.InboxTicketListResponse

data class InboxUiState(
    val idMessage: String = "",
    val welcomeMessage: String = "",
    val showChatBotWidget: Boolean = false,
    val errorMessageChatBotWidget: String = "",
    val unReadNotification: Boolean = false,
    val offset: Int = 1,
    val ticketItems: List<InboxTicketListResponse.Ticket.Data.TicketItem> = arrayListOf(),
    val isFilteredData: Boolean = false,
    val isChatbotActive: Boolean = false,
    val exception: Exception? = null
)

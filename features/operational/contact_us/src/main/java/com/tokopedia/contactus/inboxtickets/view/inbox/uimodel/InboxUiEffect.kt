package com.tokopedia.contactus.inboxtickets.view.inbox.uimodel

import com.tokopedia.contactus.inboxtickets.data.model.InboxTicketListResponse

sealed class InboxUiEffect {
    data class FetchInboxError(val throwable: Throwable) : InboxUiEffect()
    data class LoadNextPageSuccess(
        val isFirstPage: Boolean = true,
        val isHasNext: Boolean = false,
        val allItems: List<InboxTicketListResponse.Ticket.Data.TicketItem>,
        val currentPageItems: List<InboxTicketListResponse.Ticket.Data.TicketItem>
    ) : InboxUiEffect()
    data class EmptyTicket(val name: String, val isFilteredData: Boolean = false) : InboxUiEffect()
}

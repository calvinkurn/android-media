package com.tokopedia.contactus.inboxtickets.view.ticket.uimodel

import com.tokopedia.contactus.inboxtickets.data.model.Tickets
import com.tokopedia.csat_rating.data.BadCsatReasonListItem

data class TicketUiState(
    val isLoading: Boolean = true,
    val ticketDetail : Tickets = Tickets(),
    val csatReasonListBadReview : List<BadCsatReasonListItem> = arrayListOf(),
    val isIssueClose : Boolean = false
)

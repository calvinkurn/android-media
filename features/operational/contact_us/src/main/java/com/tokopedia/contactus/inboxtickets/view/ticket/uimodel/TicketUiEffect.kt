package com.tokopedia.contactus.inboxtickets.view.ticket.uimodel

import com.tokopedia.contactus.inboxtickets.domain.CommentsItem
import com.tokopedia.contactus.inboxtickets.view.ticket.TicketViewModel.Companion.FIND_KEYWORD

sealed class TicketUiEffect {
    data class SendCSATRatingSuccess(
        val ticketNumber: String = "",
        val reason: String = "",
        val rating: Int = 0
    ) : TicketUiEffect()

    data class SendCSATRatingFailed(val messageError: String = "", val throwable: Throwable? = null) : TicketUiEffect()

    data class GetDetailTicketFailed(
        val messageError: String = "",
        val throwable: Throwable? = null
    ) : TicketUiEffect()

    data class OnSearchTicketKeyword(
        val isOnProgress: Boolean = false,
        val searchKeyword: String = "",
        val sizeSearch: Int = 0
    ) : TicketUiEffect()

    data class OnSearchTicketKeywordFailed(val throwable: Throwable? = null) : TicketUiEffect()

    data class OnCloseTicketSuccess(val ticketNumber: String = "") : TicketUiEffect()

    data class OnCloseTicketFailed(val messageError: String = "", val throwable: Throwable? = null) : TicketUiEffect()

    data class SendTextMessageSuccess(val commentItems: CommentsItem) : TicketUiEffect()

    data class SendTextMessageFailed(
        val messageError: String = "",
        val throwable: Throwable? = null
    ) : TicketUiEffect()

    data class OnSendRatingSuccess(
        val rating: Int,
        val commentItem: CommentsItem = CommentsItem(),
        val commentPosition: Int = 0
    ) : TicketUiEffect()

    data class OnSendRatingFailed(val messageError: String = "", val throwable: Throwable? = null) : TicketUiEffect()
}

data class OnFindKeywordAtTicket(
    val positionAdapter: Int = 0,
    val positionKeyword: Int = 0,
    val status: Int = FIND_KEYWORD
)

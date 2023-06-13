package com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel

import com.tokopedia.contactus.inboxtickets.domain.CommentsItem
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailViewModel.Companion.FIND_KEYWORD

sealed class InboxDetailUiEffect {
    data class SendCSATRatingSuccess(
        val ticketNumber: String = "",
        val reason: String = "",
        val rating: Int = 0
    ) : InboxDetailUiEffect()

    data class SendCSATRatingFailed(
        val messageError: String = "",
        val throwable: Throwable? = null
    ) : InboxDetailUiEffect()

    data class GetDetailInboxDetailFailed(
        val messageError: String = "",
        val throwable: Throwable? = null
    ) : InboxDetailUiEffect()

    data class OnSearchInboxDetailKeyword(
        val isOnProgress: Boolean = false,
        val searchKeyword: String = "",
        val sizeSearch: Int = 0
    ) : InboxDetailUiEffect()

    data class OnSearchInboxDetailKeywordFailed(val throwable: Throwable? = null) :
        InboxDetailUiEffect()

    data class OnCloseInboxDetailSuccess(val ticketNumber: String = "") : InboxDetailUiEffect()

    data class OnCloseInboxDetailFailed(
        val messageError: String = "",
        val throwable: Throwable? = null
    ) : InboxDetailUiEffect()

    data class SendTextMessageSuccess(val commentItems: CommentsItem) : InboxDetailUiEffect()

    data class SendTextMessageFailed(
        val messageError: String = "",
        val throwable: Throwable? = null
    ) : InboxDetailUiEffect()

    data class OnSendRatingSuccess(
        val rating: Int,
        val commentItem: CommentsItem = CommentsItem(),
        val commentPosition: Int = 0
    ) : InboxDetailUiEffect()

    data class OnSendRatingFailed(val messageError: String = "", val throwable: Throwable? = null) :
        InboxDetailUiEffect()
}

data class OnFindKeywordAtTicket(
    val positionAdapter: Int = 0,
    val positionKeyword: Int = 0,
    val status: Int = FIND_KEYWORD
)

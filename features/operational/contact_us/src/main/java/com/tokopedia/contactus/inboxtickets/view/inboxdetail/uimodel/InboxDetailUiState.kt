package com.tokopedia.contactus.inboxtickets.view.inboxdetail.uimodel

import com.tokopedia.contactus.inboxtickets.data.model.Tickets
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.dynamiccsat.domain.model.CsatModel

data class InboxDetailUiState(
    val isLoading: Boolean = true,
    val ticketDetail: Tickets = Tickets(),
    val csatReasonListBadReview: List<BadCsatReasonListItem> = arrayListOf(),
    val dynamicCsat: CsatModel = CsatModel(),
    val isIssueClose: Boolean = false
)

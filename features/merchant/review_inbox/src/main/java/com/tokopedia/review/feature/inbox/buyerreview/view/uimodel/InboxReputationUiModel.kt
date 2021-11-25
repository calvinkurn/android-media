package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

/**
 * @author by nisie on 8/15/17.
 */
data class InboxReputationUiModel(
    var list: List<InboxReputationItemUiModel> = listOf(),
    var isHasNextPage: Boolean = false
)
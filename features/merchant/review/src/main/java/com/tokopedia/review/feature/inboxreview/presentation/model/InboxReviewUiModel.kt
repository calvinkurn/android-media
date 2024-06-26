package com.tokopedia.review.feature.inboxreview.presentation.model

import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel

data class InboxReviewUiModel(
        var feedbackInboxList: List<FeedbackInboxUiModel> = listOf(),
        var filterBy: String = "",
        var page: Int = 0,
        var hasNext: Boolean = false,
        var remainder: Int = 0,
        var useAutoReply: Boolean = false
)
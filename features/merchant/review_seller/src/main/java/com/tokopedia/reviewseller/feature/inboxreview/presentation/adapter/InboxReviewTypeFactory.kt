package com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter

import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewEmptyUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewErrorUiModel

interface InboxReviewTypeFactory {
    fun type(inboxReviewEmptyUiModel: InboxReviewEmptyUiModel): Int
    fun type(inboxReviewErrorUiModel: InboxReviewErrorUiModel): Int
    fun type(feedbackInboxUiModel: FeedbackInboxUiModel): Int
}
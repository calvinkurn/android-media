package com.tokopedia.review.feature.inboxreview.presentation.adapter

import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewEmptyUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewErrorUiModel

interface InboxReviewTypeFactory {
    fun type(inboxReviewEmptyUiModel: InboxReviewEmptyUiModel): Int
    fun type(inboxReviewErrorUiModel: InboxReviewErrorUiModel): Int
    fun type(feedbackInboxUiModel: FeedbackInboxUiModel): Int
}
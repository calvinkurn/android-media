package com.tokopedia.review.feature.inboxreview.presentation.adapter

import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel

interface FeedbackInboxReviewListener {
    fun onItemReplyOrEditClicked(data: FeedbackInboxUiModel, isEmptyReply: Boolean, adapterPosition: Int)
    fun onImageItemClicked(titleProduct: String, imageUrls: List<String>, thumbnailsUrl: List<String>, feedbackId: String, productId: String, position: Int)
    fun onBackgroundMarginIsReplied(isNotReplied: Boolean)
    fun onInFullReviewClicked(feedbackId: String, productId: String)
    fun isFirstTimeSeeKejarUlasan(): Boolean
}

interface GlobalErrorStateListener {
    fun onActionGlobalErrorStateClicked()
}

interface RatingListListener {
    fun onItemRatingClicked(starSelected: String, selected: Boolean, adapterPosition: Int)
}
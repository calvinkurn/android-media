package com.tokopedia.review.feature.inboxreview.presentation.adapter

import android.view.View
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel

interface FeedbackInboxReviewListener {
    fun onItemReplyOrEditClicked(data: FeedbackInboxUiModel, isEmptyReply: Boolean, adapterPosition: Int)
    fun onImageItemClicked(titleProduct: String, imageUrls: List<String>, thumbnailsUrl: List<String>, feedbackId: String, productId: String, position: Int)
    fun onBackgroundMarginIsReplied(isNotReplied: Boolean)
    fun onInFullReviewClicked(feedbackId: String, productId: String)
    fun showCoachMark(view: View?)
    fun hideCoachMark()
}

interface GlobalErrorStateListener {
    fun onActionGlobalErrorStateClicked()
}

interface RatingListListener {
    fun onItemRatingClicked(starSelected: String, selected: Boolean, adapterPosition: Int)
}
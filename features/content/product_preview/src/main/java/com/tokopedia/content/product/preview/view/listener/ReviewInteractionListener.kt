package com.tokopedia.content.product.preview.view.listener

import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewContentUiModel

interface ReviewInteractionListener {
    fun onReviewMediaScrolled()
    fun onPauseResumeVideo()
    fun onReviewCredibilityClicked(author: ReviewAuthorUiModel)
    fun onMenuClicked()
    fun onLike(isDoubleTap: Boolean)
    fun updateReviewWatchMode()
    fun onShareClicked(item: ReviewContentUiModel, selectedMediaId: String)
}

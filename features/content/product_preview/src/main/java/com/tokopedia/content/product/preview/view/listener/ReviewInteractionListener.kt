package com.tokopedia.content.product.preview.view.listener

import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMenuStatus

interface ReviewInteractionListener {
    fun onReviewCredibilityClicked(author: ReviewAuthorUiModel)
    fun onMenuClicked(menu: ReviewMenuStatus)
    fun onLike(status: ReviewLikeUiState)
}

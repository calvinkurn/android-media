package com.tokopedia.review.feature.inbox.pending.data.mapper

import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedback
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponseWrapper
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel

object ReviewPendingMapper {

    fun mapProductRevWaitForFeedbackResponseToReviewPendingUiModel(productrevWaitForFeedbackList: List<ProductrevWaitForFeedback>): List<ReviewPendingUiModel> {
        return productrevWaitForFeedbackList.map {
            ReviewPendingUiModel(it)
        }
    }

}
package com.tokopedia.review.feature.inbox.pending.data.mapper

import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponseWrapper
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel

object ReviewPendingMapper {

    fun mapProductRevWaitForFeedbackResponseToReviewPendingUiModel(productrevWaitForFeedbackResponseWrapper: ProductrevWaitForFeedbackResponseWrapper): List<ReviewPendingUiModel> {
        return productrevWaitForFeedbackResponseWrapper.productrevWaitForFeedbackWaitForFeedback.list.map {
            ReviewPendingUiModel(it)
        }
    }

}
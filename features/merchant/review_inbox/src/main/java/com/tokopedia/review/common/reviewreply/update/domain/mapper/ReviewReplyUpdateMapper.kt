package com.tokopedia.review.common.reviewreply.update.domain.mapper

import com.tokopedia.review.common.reviewreply.update.domain.model.ReviewReplyUpdateResponse
import com.tokopedia.review.common.reviewreply.update.presenter.model.ReviewReplyUpdateUiModel

object ReviewReplyUpdateMapper {
    fun mapToUpdateReplyUiModel(
        productrevUpdateSellerResponse: ReviewReplyUpdateResponse.ProductrevUpdateSellerResponse
    ): ReviewReplyUpdateUiModel {
        return ReviewReplyUpdateUiModel(
            success = productrevUpdateSellerResponse.success,
            responseMessage = productrevUpdateSellerResponse.data.responseMessage
        )
    }
}
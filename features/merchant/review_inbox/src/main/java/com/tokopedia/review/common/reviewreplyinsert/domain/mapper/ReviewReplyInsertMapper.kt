package com.tokopedia.review.common.reviewreplyinsert.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.review.common.reviewreplyinsert.domain.model.ReviewReplyInsertResponse
import com.tokopedia.review.common.reviewreplyinsert.presentation.model.ReviewReplyInsertUiModel

object ReviewReplyInsertMapper {
    fun mapToInsertReplyUiModel(
        productrevInsertSellerResponse: ReviewReplyInsertResponse.ProductrevInsertSellerResponse
    ): ReviewReplyInsertUiModel {
        return ReviewReplyInsertUiModel(productrevInsertSellerResponse.success.orFalse())
    }
}